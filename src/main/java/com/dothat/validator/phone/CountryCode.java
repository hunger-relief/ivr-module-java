package com.dothat.validator.phone;

import com.dothat.location.data.Country;

/**
 * Enum for all Country codes supported by the system.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum CountryCode {
  INDIA("91", Country.INDIA, new PhoneFormat(10)),
  ;
  
  private final String code;
  private final Country country;
  private final PhoneFormat format;
  
  CountryCode(String code, Country country, PhoneFormat format) {
    this.code = code;
    this.country = country;
    this.format = format;
  }
  
  public String getCode() {
    return code;
  }
  
  public Country getCountry() {
    return country;
  }
  
  public PhoneFormat getFormat() {
    return format;
  }
}
