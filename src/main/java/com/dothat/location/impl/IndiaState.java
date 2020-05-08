package com.dothat.location.impl;

import com.dothat.location.StateResolver;
import com.dothat.location.data.Country;
import com.dothat.location.data.State;

/**
 * Enum for States of India.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum IndiaState implements State<IndiaState> {
  BIHAR,
  DELHI,
  HARYANA,
  KARNATAKA,
  KERALA,
  MAHARASHTRA,
  PUNJAB,
  RAJASTHAN,
  TAMIL_NADU,
  TELENGANA,
  WEST_BENGAL,
  ;
  
  @Override
  public Class<IndiaState> getEnumClass() {
    return IndiaState.class;
  }
  
  @Override
  public Country getCountry() {
    return Country.INDIA;
  }
  
  @Override
  public String getCode() {
    return name();
  }
  
  public static class Resolver implements StateResolver {
  
    @Override
    public State resolve(String code) {
      return IndiaState.valueOf(code);
    }
  }
}
