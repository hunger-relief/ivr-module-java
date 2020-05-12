package com.dothat.relief.request.data;

/**
 * Enum for the Verification Status of the Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum VerificationStatus {
  UNVERIFIED("Unverified"),

  INFO_REQUESTED("Info Requested"),
  INFO_RECEIVED("Info Received"),

  REVIEW_REQUESTED("Review Requested"),
  REVIEW_PROCESSING("Processing Review"),
  
  VERIFIED("Verified"),
  FLAGGED("Flagged"),
  ;
  
  private String displayValue;
  
  VerificationStatus(String displayValue) {
    this.displayValue = displayValue;
  }
  
  public String getDisplayValue() {
    return displayValue;
  }
}
