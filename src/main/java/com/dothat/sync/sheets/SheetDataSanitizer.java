package com.dothat.sync.sheets;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class SheetDataSanitizer {
  
  public static String sanitizePhoneNumber(String number) {
    if (number == null) {
      return "";
    }
    // Strip out the country code if any and add ' to make it a string literal
    int index = number.indexOf("-");
    return "'" + number.substring(index + 1);
  }
}
