package com.dothat.ivr.mapping.servlet;

import com.dothat.common.field.Field;
import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.field.error.FieldError;
import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.location.data.Country;
import com.dothat.location.data.Location;
import com.dothat.relief.request.data.RequestType;
import com.google.common.base.Strings;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extracts the IVR Mapping from a Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
class IVRMappingExtractor {
  
  IVRMapping extract(JSONObject json) {
    IVRMapping data = new IVRMapping();
    List<FieldError> errorList = new ArrayList<>();
    FieldValueExtractor extractor = new FieldValueExtractor(errorList);
    extractor.init(getFieldNameMap());
    
    String countryValue = extractor.extract(json, IVRMappingField.COUNTRY);
    Country country = null;
    if (!Strings.isNullOrEmpty(countryValue)) {
      country = Country.valueOf(countryValue);
    }
    if (country == null) {
      throw new IllegalArgumentException("Must specify country for the phone number being mapped");
    }
    String phone = extractor.extractPhone(country, json, IVRMappingField.PHONE, true);
    if (Strings.isNullOrEmpty(phone)) {
      throw new IllegalArgumentException("Must specify phone that is being mapped");
    }
    data.setPhoneNumber(phone);
    String circle = extractor.extract(json, IVRMappingField.CIRCLE, false);
    data.setCircle(circle);
  
    String location = extractor.extract(json, IVRMappingField.LOCATION_ID, true);
    if (location == null) {
      throw new IllegalArgumentException("Must specify ID of the location that the phone number is being mapped to");
    }
    try {
      Location locationData = new Location();
      locationData.setLocationId(Long.valueOf(location));
      data.setLocation(locationData);
    } catch (Throwable t) {
      throw new IllegalArgumentException("Location ID must be a valid number");
    }
    String requestType = extractor.extract(json, IVRMappingField.REQUEST_TYPE, true);
    if (requestType == null) {
      throw new IllegalArgumentException("Must specify ID of the location that the phone number is being mapped to");
    }
    try {
      data.setRequestType(RequestType.valueOf(requestType));
    } catch (Throwable t) {
      throw new IllegalArgumentException("Invalid Request Type " + requestType);
    }
    return data;
  }
  
  private Map<Field, String> getFieldNameMap() {
    Map<Field, String> map = new HashMap<>();
    for (IVRMappingField field : IVRMappingField.values()) {
      map.put(field, field.getParamName());
    }
    return map;
  }
}
