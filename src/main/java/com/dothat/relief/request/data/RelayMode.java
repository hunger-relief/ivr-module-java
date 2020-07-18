package com.dothat.relief.request.data;

/**
 * Enum for the urgency with which the Service Request needs to be relayed to the Destination.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum RelayMode {
  REALTIME("Realtime"),
  BATCH("Batch"),
  ;

  private String displayValue;

  RelayMode(String displayValue) {
    this.displayValue = displayValue;
  }

  public String getDisplayValue() {
    return displayValue;
  }
}
