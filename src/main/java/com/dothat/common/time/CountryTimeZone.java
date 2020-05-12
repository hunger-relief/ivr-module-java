package com.dothat.common.time;

import com.dothat.location.data.Country;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

/**
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum CountryTimeZone {
  INDIA(Country.INDIA, DateTimeZone.forID("Asia/Kolkata")),
  ;
  private final Country country;
  private final DateTimeZone timeZone;
  
  
  CountryTimeZone(Country country, DateTimeZone timeZone) {
    this.country = country;
    this.timeZone = timeZone;
  }
  
  public Country getCountry() {
    return country;
  }
  
  public DateTimeZone getTimeZone() {
    return timeZone;
  }
}
