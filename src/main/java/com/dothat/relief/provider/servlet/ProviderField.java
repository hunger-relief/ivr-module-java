package com.dothat.relief.provider.servlet;

import com.dothat.common.field.Field;

/**
 * Enum for Fields of a Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum ProviderField implements Field {
  CODE("code"),
  SHEET_ID("sheet_id"),
  ;
  private final String paramName;
  
  ProviderField(String paramName) {
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
