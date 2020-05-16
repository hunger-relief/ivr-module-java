package com.dothat.ivr.mapping.servlet;

import com.dothat.common.field.Field;
import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.field.error.FieldError;
import com.dothat.ivr.mapping.data.IVRNodeMapping;
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
class IVRNodeMappingExtractor {
  
  IVRNodeMapping extract(JSONObject json) {
    IVRNodeMapping data = new IVRNodeMapping();
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
  
    String nodeId = extractor.extract(json, IVRMappingField.NODE_ID, true);
    String response = extractor.extract(json, IVRMappingField.RESPONSE, true);
    if (Strings.isNullOrEmpty(nodeId) || Strings.isNullOrEmpty(response)) {
      throw new IllegalArgumentException("Both Node Id and response on the node " +
          "have to be specified for the mapping");
    }
    data.setNodeId(nodeId);
    data.setResponse(response);
  
    String attributeName = extractor.extract(json, IVRMappingField.ATTRIBUTE_NAME, true);
    if (Strings.isNullOrEmpty(attributeName)) {
      throw new IllegalArgumentException("Attribute Name is required for the mapping");
    }
    data.setAttributeName(attributeName);
  
    String attributeValue = extractor.extract(json, IVRMappingField.ATTRIBUTE_NAME, false);
    String location = extractor.extract(json, IVRMappingField.LOCATION_ID, false);
    String requestType = extractor.extract(json, IVRMappingField.REQUEST_TYPE, false);
    int numValues = 0;
    if (!Strings.isNullOrEmpty(attributeValue)) {
      numValues++;
      data.setAttributeValue(attributeValue);
    }
    if (!Strings.isNullOrEmpty(location)) {
      numValues++;
      try {
        Location locationData = new Location();
        locationData.setLocationId(Long.valueOf(location));
        data.setLocation(locationData);
      } catch (Throwable t) {
        throw new IllegalArgumentException("Location ID must be a valid number");
      }
    }
    if (!Strings.isNullOrEmpty(requestType)) {
      numValues++;
      try {
        data.setRequestType(RequestType.valueOf(requestType));
      } catch (Throwable t) {
        throw new IllegalArgumentException("Invalid Request Type " + requestType);
      }
    }
    if (numValues == 0) {
      throw new IllegalArgumentException("At least one of attribute Value, location, or request type must " +
          "be mapped to the IVR node");
    }
    if (numValues > 1) {
      throw new IllegalArgumentException("Only one of attribute Value, location, or request type may " +
          "be mapped to the IVR node");
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
