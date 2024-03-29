package com.dothat.relief.provider.servlet;

import com.dothat.common.field.Field;

/**
 * Enum for Fields of a Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum AssignInstructionField implements Field {
  PROVIDER("provider"),
  SOURCE("source"),
  SOURCE_TYPE("source_type"),
  PHONE("phone"),
  COUNTRY("country"),
  LOCATION_ID("location_id"),
  REQUEST_TYPE("request_type"),
  ;
  private final String paramName;
  
  AssignInstructionField(String paramName) {
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
