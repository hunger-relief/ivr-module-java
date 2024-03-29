package com.dothat.common.validate.phone.impl;

import com.dothat.common.validate.phone.CountryPhoneValidator;

/**
 * Validates the Phone Number for India
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IndiaPhoneValidator implements CountryPhoneValidator {

  @Override
  public void validate(String number) throws IllegalArgumentException {
    if (number.length() != 10) {
      throw new IllegalArgumentException("Phone Number for India can have only 10 digits : " + number);
    }
  }
}
