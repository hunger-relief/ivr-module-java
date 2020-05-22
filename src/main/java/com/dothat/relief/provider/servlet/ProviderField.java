package com.dothat.relief.provider.servlet;

import com.dothat.common.field.Field;

/**
 * Enum for Fields of a Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum ProviderField implements Field {
  CODE("code"),
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
