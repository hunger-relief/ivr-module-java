package com.dothat.relief.request.data;

/**
 * Enum for the type of Service requested.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum RequestType {
  AMBULANCE("Ambulance"),
  CALLBACK("Callback"),
  COOKED_FOOD("Cooked Food"),
  DOCTOR("Doctor"),
  JOBS("Jobs"),
  MEDICINE("Medicines"),
  MIGRATION("Migration"),
  MONEY("Financial Help"),
  RATION("Ration"),
  UNKNOWN("Not Sure"),
  VACCINE("Vaccine"),
  ;
  
  private String displayValue;
  
  RequestType(String displayValue) {
    this.displayValue = displayValue;
  }
  
  public String getDisplayValue() {
    return displayValue;
  }
}
