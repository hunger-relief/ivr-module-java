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
    // Remove all non digits
    String phone = number.replaceAll("[^0-9]", "");

    // Remove all leading zeros
    phone = phone.replaceAll("^[0]+", "");
    
    CountryCode code = CountryCodeLookup.getInstance().getCode(country);
    String countryCode = code.getCode();
    PhoneFormat format = code.getFormat();

    // Remove country code from the beginning
    if (phone.startsWith(countryCode) && phone.length() > format.getMaxDigits()) {
      phone = phone.substring(countryCode.length());
    }

    // Add country code in the correct format
    phone = "+" + countryCode + "-" + phone;
    return phone;
  }
}
