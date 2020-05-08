package com.dothat.location.data;

/**
 * Interface for enums that provide country specific states.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface State<T extends Enum<T>> {
  
  Class<T> getEnumClass();
  
  Country getCountry();
  
  String getCode();
}
