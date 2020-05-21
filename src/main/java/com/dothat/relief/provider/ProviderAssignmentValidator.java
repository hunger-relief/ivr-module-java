package com.dothat.relief.provider;

import com.dothat.common.validate.PhoneNumberSanitizer;
import com.dothat.common.validate.PhoneNumberValidator;
import com.dothat.location.LocationDisplayUtils;
import com.dothat.location.LocationService;
import com.dothat.location.data.Country;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ProviderAssignment;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import com.google.common.base.Strings;

/**
 * Validates a Provider Assignment.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProviderAssignmentValidator {
  
  void validate(ProviderAssignment data) {
    if (data.getProvider() == null) {
      throw new IllegalArgumentException("Must specify the Provider that is being configured.");
    }
    String providerCode = data.getProvider().getProviderCode();
    Long providerId = data.getProvider().getProviderId();
    if (!Strings.isNullOrEmpty(providerCode)) {
      ReliefProvider provider = new ReliefProviderService().lookupByCode(providerCode);
      if (provider == null) {
        throw new IllegalArgumentException("No provider found with code " + providerCode);
      }
      data.setProvider(provider);
    } else if (providerId != null) {
      ReliefProvider provider = new ReliefProviderService().lookupById(providerId);
      if (provider == null) {
        throw new IllegalArgumentException("No provider found with Id " + providerId);
      }
      data.setProvider(provider);
    } else {
      throw new IllegalArgumentException("Must specify either the code or the id for the Provider");
    }
    
    if (data.getLocation() != null) {
      Long locationId = data.getLocation().getLocationId();
      if (locationId != null) {
        Location location = new LocationService().lookup(locationId);
        if (location == null) {
          throw new IllegalArgumentException("No location found with Id " + locationId);
        }
        data.setLocation(location);
      }
    }

    RequestType requestType = data.getRequestType();
    if (requestType == null) {
      throw new IllegalArgumentException("Must specify Request Type to assign requests to "
          + data.getProvider().getProviderCode());
    }
  
    if (data.getSource() != null) {
      SourceType sourceType = data.getSource().getSourceType();
      String phoneNumber = data.getSource().getDialedNumber();
      Country country = data.getSource().getCountry();
      String source = data.getSource().getSource();

      if (sourceType == null) {
        throw new IllegalArgumentException("Must specify Request Source Type to assign it to "
            + data.getProvider().getProviderCode());
      } else if (sourceType == SourceType.IVR) {
        if (Strings.isNullOrEmpty(phoneNumber) || country == null) {
          throw new IllegalArgumentException("Phone number " + phoneNumber + " and Country " + country
              + " from " + sourceType + " " + source + " are required fields to assign it to "
              + data.getProvider().getProviderCode());
        }
      }
      String dialedNumber = new PhoneNumberSanitizer(country).sanitize(phoneNumber);
      new PhoneNumberValidator().validate(dialedNumber);
      
      data.getSource().setDialedNumber(dialedNumber);
    } else {
      if (data.getLocation() == null) {
        throw new IllegalArgumentException("Must specify either Location or Request Source for Request Type "
            + requestType + " to assign it to " + data.getProvider().getProviderCode());
      }
    }
  }
}
