package com.dothat.validator;

import com.dothat.location.data.Country;
import com.dothat.validator.phone.CountryCodeLookup;
import com.dothat.validator.phone.CountryPhoneValidator;
import com.dothat.validator.phone.PhoneValidatorRegistry;

import java.util.regex.Pattern;

/**
 * Validates that the phone number passed in is valid.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class PhoneNumberValidator {

  private static final Pattern IS_NUMERIC_PATTERN = Pattern.compile("\\d+");
  
  public void validate(String phoneNumber) throws IllegalArgumentException {
    // Should start with country code, eg +91-98100981000
    if (!phoneNumber.startsWith("+") || !phoneNumber.contains("-")) {
      throw new IllegalArgumentException("Phone number does not match the expected pattern +<Country Code>-<Number>" +
          " eg +91-9810098100");
    }
    String countryCode = "";
    String number = "";
    int dashIndex = phoneNumber.indexOf("-");
    int countryStartIndex = 1;
    int countryEndIndex = dashIndex - 1;
    int numberStartIndex = dashIndex + 1;

    // Extract just the Country Code from the formatted phone number
    if (countryStartIndex <= countryEndIndex) {
      countryCode = phoneNumber.substring(countryStartIndex, countryEndIndex);
    }
    // Extract just the Number from the formatted phone number
    if (phoneNumber.length() > numberStartIndex) {
      number = phoneNumber.substring(numberStartIndex);
    }

    // Make sure the number has a country code.
    if (countryCode.isEmpty()) {
      throw new IllegalArgumentException("Phone number doesn't have a country code");
    }
    // Make sure the country code is supported by the system.
    Country country = CountryCodeLookup.getInstance().getCountry(countryCode);
    if (country == null) {
      throw new IllegalArgumentException("Country code " + countryCode + " not supported by the System");
    }
  
  
    // Make sure the number is provided.
    if (number.isEmpty()) {
      throw new IllegalArgumentException("Phone number was not specified");
    }
    // Make sure the number only has numeric chars.
    if (!isNumeric(number)) {
      throw new IllegalArgumentException("Phone number contains non numeric character " + number);
    }
    
    // Based on Country, lookup and lookup Country specific Validator, if any
    CountryPhoneValidator validator = PhoneValidatorRegistry.getInstance().getValidator(country);
    if (validator != null) {
      validator.validate(number);
    }
  }
  
  private boolean isNumeric(String strNum) {
    if (strNum == null || strNum.isEmpty()) {
      return false;
    }
    return IS_NUMERIC_PATTERN.matcher(strNum).matches();
  }
}
