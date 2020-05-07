package com.dothat.validator.phone;

import com.dothat.location.data.Country;

import java.util.HashMap;
import java.util.Map;

/**
 * Lookup the Country by Country Code.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CountryCodeLookup {
  private static final CountryCodeLookup INSTANCE = new CountryCodeLookup();
  
  private final Map<String, Country> registry = new HashMap<>();
  
  private CountryCodeLookup() {
    for (CountryCode countryCode : CountryCode.values()) {
      registry.put(countryCode.getCode(), countryCode.getCountry());
    }
  }
  
  public static CountryCodeLookup getInstance() {
    return INSTANCE;
  }
  
  public Country getCountry(String countryCode) {
    return registry.get(countryCode);
  }
}
