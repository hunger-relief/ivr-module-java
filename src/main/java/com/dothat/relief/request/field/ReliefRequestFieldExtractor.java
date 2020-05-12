package com.dothat.relief.request.field;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.relief.request.data.ReliefRequest;
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
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("MMM dd hh:mm");
  
  public Map<String, String> generateFieldValueMap(String phone, ReliefRequest data) {
    Map<String, String> map = new HashMap<>();
    DateTime timestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    if (timestamp == null) {
      timestamp = DateTime.now();
    }
    map.put(RequestField.REQUEST_DATE.name(), DATE_FORMAT.print(timestamp));
    
    if (data.getRequesterID() != null) {
      map.put(RequestField.UUID.name(), data.getRequesterID().getIdentifier());
    }
    
    addString(map, RequestField.PHONE, phone);
    addString(map, RequestField.REQUEST_TYPE, data.getRequestType().getDisplayValue());

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
    // TODO(abhideep): Take Source Type into Account
    addString(map, RequestField.REQUEST_SOURCE, data.getSource());
    addString(map, RequestField.CLAIM_STATUS, data.getClaimStatus().getDisplayValue());
    if (data.getProvider() != null) {
      addString(map, RequestField.PROVIDER_CODE, data.getProvider().getProviderCode());
    }
    addString(map, RequestField.VERIFY_STATUS, data.getVerificationStatus().getDisplayValue());
    addString(map, RequestField.REQUEST_STATUS, data.getRequestStatus().getDisplayValue());
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
}
