package com.dothat.location;

import com.dothat.location.data.Country;
import com.dothat.location.data.State;
import com.dothat.location.impl.IndiaState;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility to lookup State Enum values for a Country.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class StateLookup {
  private static final StateLookup INSTANCE = new StateLookup();
  
  private final Map<Country, StateResolver> stateResolverMap = new HashMap<>();
  
  private StateLookup() {
    stateResolverMap.put(Country.INDIA, new IndiaState.Resolver());
  }
  
  public static StateLookup getInstance() {
    return INSTANCE;
  }

  public State getEnum(Country country, String code) {
    StateResolver resolver = stateResolverMap.get(country);
    return resolver.resolve(code);
  }
}
