package com.dothat.sync.destination.servlet;

import com.dothat.common.field.Field;

/**
 * Enum for Fields of a Destination.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum DestinationField implements Field {
  LOCATION("location"),
  PROVIDER("provider"),
  REQUEST_TYPE("request_type"),
  DESTINATION_TYPE("destination_type"),
  GOOGLE_SHEET_ID("sheet_id"),
  ;
  private final String paramName;
  
  DestinationField(String paramName) {
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
