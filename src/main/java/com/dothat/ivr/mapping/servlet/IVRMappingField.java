package com.dothat.ivr.mapping.servlet;

import com.dothat.common.field.Field;

/**
 * Enum for Fields of IVR Mapping.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum IVRMappingField implements Field {
  COUNTRY("country"),
  PHONE("phone"),
  CIRCLE("circle"),
  NODE_ID("node_id"),
  RESPONSE("response"),
  ATTRIBUTE_NAME("attr_name"),
  ATTRIBUTE_VALUE("attr_value"),
  LOCATION_ID("location_id"),
  REQUEST_TYPE("request_type"),
  ;
  private final String paramName;
  
  IVRMappingField(String paramName) {
    this.paramName = paramName;
  }
  
  public String getParamName() {
    return paramName;
  }
  
  @Override
  public String getFieldName() {
    return name();
  }
}
