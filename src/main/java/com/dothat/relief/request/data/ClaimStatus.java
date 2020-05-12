package com.dothat.relief.request.data;

/**
 * Enum for the Status of whether the Request has been Claimed.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum ClaimStatus {
  UNCLAIMED("Unclaimed"),
  CLAIMED("Claimed"),
  RETURNED("Returned"),
  ;
  
  private String displayValue;
  
  ClaimStatus(String displayValue) {
    this.displayValue = displayValue;
  }
  
  public String getDisplayValue() {
    return displayValue;
  }
}
