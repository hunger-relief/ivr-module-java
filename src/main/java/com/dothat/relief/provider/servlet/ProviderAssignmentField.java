package com.dothat.relief.provider.servlet;

import com.dothat.common.field.Field;

/**
 * Enum for Fields of a Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum ProviderAssignmentField implements Field {
  PROVIDER("provider"),
  SOURCE("source"),
  SOURCE_TYPE("source_type"),
  DIALED("dialed"),
  LOCATION("location"),
  REQUEST_TYPE("request_type"),
  ;
  private final String paramName;
  
  ProviderAssignmentField(String paramName) {
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
