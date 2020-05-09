package com.dothat.common.field.error;

import com.dothat.common.field.Field;

/**
 * Data for an Error in extracting data for a field.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FieldError {
  private final FieldErrorType errorType;
  private final Field field;
  
  public FieldError(FieldErrorType errorType, Field field) {
    this.errorType = errorType;
    this.field = field;
  }
  
  public FieldErrorType getErrorType() {
    return errorType;
  }
  
  public Field getField() {
    return field;
  }
}
