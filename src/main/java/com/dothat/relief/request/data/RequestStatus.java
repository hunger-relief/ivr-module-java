package com.dothat.relief.request.data;

/**
 * Enum for the Status of the Service Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum RequestStatus {
  RECEIVED("Received"),
  ACCEPTED("Accepted"),
  DUPLICATE("Duplicate"),
  OBSOLETE("Obsolete"),
  SCHEDULED("Scheduled"),
  DELIVERED("Delivered"),
  CANCELLED("Cancelled"),
  ;
  
  private String displayValue;
  
  RequestStatus(String displayValue) {
    this.displayValue = displayValue;
  }
  
  public String getDisplayValue() {
    return displayValue;
  }
}
