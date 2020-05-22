package com.dothat.location;

import com.dothat.location.data.Location;
import com.google.common.base.Strings;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class LocationDisplayUtils {
  
  public static String idForLog(Location location) {
    if (location == null) {
      return "";
    }
    return "" + location.getLocationId();
  }

  public static String forLog(Location location) {
    return forError(location);
  }
  
  public static String forError(Location location) {
    if (location == null) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    appendToLocation(builder, location.getLocation());
    appendToLocation(builder, location.getArea());
    appendToLocation(builder, location.getCity());
    return builder.toString();
  }
  
  private static void appendToLocation(StringBuilder builder, String detail) {
    if (!Strings.isNullOrEmpty(detail)) {
      if (builder.length() != 0) {
        builder.append(", ");
      }
      builder.append(detail);
    }
  }

}
