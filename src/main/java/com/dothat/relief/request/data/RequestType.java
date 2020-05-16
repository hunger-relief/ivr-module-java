package com.dothat.relief.request.data;

/**
 * Enum for the type of Service requested.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum RequestType {
  COOKED_FOOD("Cooked Food"),
  RATION("Ration"),
  MEDICINE("Medicines"),
  MIGRATION("Migration"),
  MONEY("Financial Help"),
  UNKNOWN("Not Sure"),
  ;
  
  private String displayValue;
  
  RequestType(String displayValue) {
    this.displayValue = displayValue;
  }
  
  public String getDisplayValue() {
    return displayValue;
  }
}
