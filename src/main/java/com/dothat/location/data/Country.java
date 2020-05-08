package com.dothat.location.data;

/**
 * Enum for countries supported by the system.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum Country {
  INDIA("IN"),
  UNKNOWN(""),
  ;
  
  private String isoCode;
  
  Country(String isoCode) {
    this.isoCode = isoCode;
  }
  
  public String getIsoCode() {
    return isoCode;
  }
}
