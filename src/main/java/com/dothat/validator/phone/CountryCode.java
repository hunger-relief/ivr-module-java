package com.dothat.validator.phone;

import com.dothat.location.data.Country;

/**
 * Enum for all Country codes supported by the system.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum CountryCode {
  INDIA("91", Country.INDIA),
  ;
  
  private final String code;
  private final Country country;
  
  CountryCode(String code, Country country) {
    this.code = code;
    this.country = country;
  }
  
  public String getCode() {
    return code;
  }
  
  public Country getCountry() {
    return country;
  }
}
