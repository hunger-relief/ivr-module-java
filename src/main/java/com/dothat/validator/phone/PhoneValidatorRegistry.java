package com.dothat.validator.phone;

import com.dothat.location.data.Country;
import com.dothat.validator.phone.impl.IndiaPhoneValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for all Phone Patterns supported by the System.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class PhoneValidatorRegistry {
  private static final PhoneValidatorRegistry INSTANCE = new PhoneValidatorRegistry();
  
  private final Map<Country, CountryPhoneValidator> registry = new HashMap<>();
  
  private PhoneValidatorRegistry() {
    // TODO(abhideep): Inject via Dependency Injection or App Initializer
    registry.put(Country.INDIA, new IndiaPhoneValidator());
  }
  
  public static PhoneValidatorRegistry getInstance() {
    return INSTANCE;
  }
  
  public CountryPhoneValidator getValidator(Country country) {
    return registry.get(country);
  }
}
