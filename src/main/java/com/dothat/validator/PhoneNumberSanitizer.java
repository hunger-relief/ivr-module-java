package com.dothat.validator;

import com.dothat.location.data.Country;
import com.dothat.validator.phone.CountryCode;
import com.dothat.validator.phone.CountryCodeLookup;
import com.dothat.validator.phone.PhoneFormat;

/**
 * Sanitizers a phone number.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class PhoneNumberSanitizer {
  private final Country country;
  
  public PhoneNumberSanitizer(Country country) {
    this.country = country;
  }
  
  public String sanitize(String number) {
    String phone = number.replaceAll("[^0-9]", "");
    
    CountryCode code = CountryCodeLookup.getInstance().getCode(country);
    String countryCode = code.getCode();
    PhoneFormat format = code.getFormat();
    
    if (phone.startsWith(countryCode) && phone.length() > format.getMaxDigits()) {
      phone = phone.substring(countryCode.length());
    }
    phone = "+" + countryCode + "-" + phone;
    return phone;
  }
}
