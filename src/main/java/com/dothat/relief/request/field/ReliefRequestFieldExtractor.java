package com.dothat.relief.request.field;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.time.CountryTimeZoneLookup;
import com.dothat.location.data.Country;
import com.dothat.relief.request.data.ReliefRequest;
import com.google.common.base.Strings;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * Extract all the fields fromm a Relief Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefRequestFieldExtractor {
  
  public Map<String, String> generateFieldValueMap(String phone, ReliefRequest data) {
    Map<String, String> map = new HashMap<>();
    DateTime timestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    if (timestamp == null) {
      timestamp = DateTime.now();
    }
    Country country = Country.UNKNOWN;
    if (data.getLocation() != null) {
      country = data.getLocation().getCountry();
    }

    map.put(RequestField.ROW_NUM.name(), "=ROW()");
    map.put(RequestField.SYNC_STATUS.name(), "Unsynced");
    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMM dd hh:mm a")
        .withZone(CountryTimeZoneLookup.getInstance().getTimeZone(country));
    map.put(RequestField.REQUEST_DATE.name(), dateFormatter.print(timestamp));
    
    if (data.getRequesterID() != null) {
      map.put(RequestField.UUID.name(), data.getRequesterID().getIdentifier());
    }
    
    addPhone(map, RequestField.PHONE, phone);
    if (data.getRequestType() != null) {
      addString(map, RequestField.REQUEST_TYPE, data.getRequestType().getDisplayValue());
    }
    addPhone(map, RequestField.RECEIVER_PHONE, data.getRequestReceiver());

    if (data.getLocation() != null) {
      addString(map, RequestField.LOCATION, data.getLocation().getLocation());
      addString(map, RequestField.AREA, data.getLocation().getArea());
      addString(map, RequestField.ZONE, data.getLocation().getZone());
      addString(map, RequestField.CITY, data.getLocation().getCity());
      if (data.getLocation().getState() != null) {
        addString(map, RequestField.STATE, data.getLocation().getState().getCode());
      }
      addEnum(map, RequestField.COUNTRY, data.getLocation().getCountry());
    }
    addString(map, RequestField.REQUEST_SOURCE, data.getSource());
    addString(map, RequestField.REQUEST_SOURCE_ID, data.getSourceId());
    addString(map, RequestField.REQUEST_SOURCE_TYPE, data.getSourceType().name());

    if (data.getClaimStatus() != null) {
      addString(map, RequestField.CLAIM_STATUS, data.getClaimStatus().getDisplayValue());
    }
    if (data.getProvider() != null) {
      addString(map, RequestField.PROVIDER_CODE, data.getProvider().getProviderCode());
    }
    if (data.getVerificationStatus() != null) {
      addString(map, RequestField.VERIFY_STATUS, data.getVerificationStatus().getDisplayValue());
    }
    if (data.getRequestStatus() != null) {
      addString(map, RequestField.REQUEST_STATUS, data.getRequestStatus().getDisplayValue());
    }
    return map;
  }
  
  void addString(Map<String, String> map, RequestField field, String value) {
    if (value != null) {
      map.put(field.name(), value);
    }
  }
  
  void addEnum(Map<String, String> map, RequestField field, Enum<?> value) {
    if (value != null) {
      map.put(field.name(), value.name());
    }
  }

  void addPhone(Map<String, String> map, RequestField field, String value) {
    if (Strings.isNullOrEmpty(value)) {
      return;
    }
    String phoneNUmber = value;
    // Strip out the country code if any and add ' to make it a string literal
    int index = phoneNUmber.indexOf("-");
    phoneNUmber = "'" + phoneNUmber.substring(index + 1);
    addString(map, field, phoneNUmber);
  }
}
