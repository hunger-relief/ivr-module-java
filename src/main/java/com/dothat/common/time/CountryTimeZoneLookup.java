package com.dothat.common.time;

import com.dothat.common.validate.phone.CountryCode;
import com.dothat.location.data.Country;
import org.joda.time.DateTimeZone;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Lookup the Country by Country Code.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CountryTimeZoneLookup {
  private static final CountryTimeZoneLookup INSTANCE = new CountryTimeZoneLookup();
  
  private final Map<Country, CountryTimeZone> zoneRegistry = new HashMap<>();
  
  private CountryTimeZoneLookup() {
    for (CountryTimeZone countryTimeZone : CountryTimeZone.values()) {
      zoneRegistry.put(countryTimeZone.getCountry(), countryTimeZone);
    }
  }
  
  public static CountryTimeZoneLookup getInstance() {
    return INSTANCE;
  }
  
  public DateTimeZone getTimeZone(Country country) {
    CountryTimeZone zone = zoneRegistry.get(country);
    if (zone == null) {
      return DateTimeZone.UTC;
    }
    return zone.getTimeZone();
  }
}
