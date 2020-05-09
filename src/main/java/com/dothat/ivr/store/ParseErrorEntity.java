package com.dothat.ivr.store;

import com.dothat.ivr.data.IVRDataField;
import com.dothat.ivr.data.ParseError;
import com.dothat.ivr.data.ParseErrorType;

/**
 * Entity that store Parse Errors
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ParseErrorEntity {
  private ParseErrorType errorType;
  private IVRDataField field;
  
  private ParseErrorEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  ParseErrorEntity(ParseError data) {
    this();
    errorType = data.getErrorType();
    field = data.getField();
  }
  
  ParseError getData() {
    return new ParseError(errorType, field);
  }
}
