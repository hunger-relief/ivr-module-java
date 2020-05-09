package com.dothat.ivr.notif.data;

/**
 * Tracks Parsing Errors of Incoming notifications.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ParseError {
  private final ParseErrorType errorType;
  private final IVRDataField field;
  
  public ParseError(ParseErrorType errorType, IVRDataField field) {
    this.errorType = errorType;
    this.field = field;
  }
  
  public ParseErrorType getErrorType() {
    return errorType;
  }
  
  public IVRDataField getField() {
    return field;
  }
}
