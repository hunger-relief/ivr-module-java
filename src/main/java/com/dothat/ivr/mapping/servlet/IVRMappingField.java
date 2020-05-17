package com.dothat.ivr.mapping.servlet;

import com.dothat.common.field.Field;

/**
 * Enum for Fields of IVR Mapping.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum IVRMappingField implements Field {
  PROVIDER("provider"),
  NODE_ID("node_id"),
  RESPONSE("response"),
  ATTRIBUTE_NAME("attr_name"),
  ATTRIBUTE_VALUE("attr_value"),
  REQUEST_TYPE("request_type"),
  LOCATION_ID("location_id"),
  COUNTRY("country"),
  PHONE("phone"),
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
