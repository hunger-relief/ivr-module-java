package com.dothat.sync.destination;

import com.dothat.location.LocationService;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.destination.data.DestinationType;
import com.google.common.base.Strings;

/**
 * Validates a Destination.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DestinationValidator {
  
  void validate(Destination data) {
    if (data.getProvider() == null) {
      throw new IllegalArgumentException("Must specify the Provider for whom this is a destination");
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
    if (data.getDestinationType() != DestinationType.GOOGLE_SHEETS) {
      throw new IllegalArgumentException("Only Google Sheets is supported as a destination for now");
    }
    if (Strings.isNullOrEmpty(data.getGoogleSheetId())) {
      throw new IllegalArgumentException("Must specify the Google Sheet Id");
    }
  }
}
