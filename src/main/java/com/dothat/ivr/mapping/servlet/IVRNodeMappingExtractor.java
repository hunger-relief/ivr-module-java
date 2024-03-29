package com.dothat.ivr.mapping.servlet;

import com.dothat.common.field.Field;
import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.field.error.FieldError;
import com.dothat.ivr.mapping.data.IVRNodeMapping;
import com.dothat.ivr.notif.data.IVRProvider;
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
  
    String providerValue = extractor.extract(json, IVRMappingField.PROVIDER);
    if (!Strings.isNullOrEmpty(providerValue)) {
      try {
        data.setProvider(IVRProvider.valueOf(providerValue));
      } catch (Throwable t) {
        throw new IllegalArgumentException("Invalid IVR Provider " + providerValue);
      }
    } else {
      throw new IllegalArgumentException("IVR Provider is required for the mapping");
    }
    
    String countryValue = extractor.extract(json, IVRMappingField.COUNTRY, false);
    Country country = null;
    if (!Strings.isNullOrEmpty(countryValue)) {
      country = Country.valueOf(countryValue);
    }
    String phone = extractor.extractPhone(country, json, IVRMappingField.PHONE, false);
    if (Strings.isNullOrEmpty(phone) && country == null) {
      throw new IllegalArgumentException("Must specify country if the phone number is being mapped");
    }
    data.setPhoneNumber(phone);
  
    String nodeId = extractor.extract(json, IVRMappingField.NODE_ID, true);
    String response = extractor.extract(json, IVRMappingField.RESPONSE, true);
    if (Strings.isNullOrEmpty(nodeId)) {
      throw new IllegalArgumentException("Node Id is required to specify the node that is being mapped");
    }
    data.setNodeId(nodeId);
    data.setResponse(response);
  
    String attributeName = extractor.extract(json, IVRMappingField.ATTRIBUTE_NAME, true);
    if (Strings.isNullOrEmpty(attributeName)) {
      throw new IllegalArgumentException("Attribute Name is required for the mapping");
    }
    data.setAttributeName(attributeName);
  
    String attributeValue = extractor.extract(json, IVRMappingField.ATTRIBUTE_VALUE, false);
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
