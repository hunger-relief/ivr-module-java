package com.dothat.location.servlet;

import com.dothat.common.field.Field;

/**
 * Enum for Fields of a Location.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum LocationField implements Field {
  LOCATION("location"),
  AREA("area"),
  ZONE("zone"),
  CITY("city"),
  STATE("state"),
  COUNTRY("country"),
  ;
  private final String paramName;
  
  LocationField(String paramName) {
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
