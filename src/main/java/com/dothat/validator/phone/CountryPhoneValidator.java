package com.dothat.validator.phone;

/**
 * Interface for Country specific validation of phone number.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface CountryPhoneValidator {
  /**
   * Validates the phone number for the given country.
   *
   * @param number Phone number without the country code.
   * @throws IllegalArgumentException If it is an invalid phone number for the country.
   */
  void validate(String number) throws IllegalArgumentException;
}
