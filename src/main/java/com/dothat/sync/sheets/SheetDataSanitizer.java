package com.dothat.sync.sheets;

import com.google.common.base.Strings;

/**
 * Sanitizes the data for
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SheetDataSanitizer {
  
  public static String sanitizePhoneNumber(String number) {
    if (Strings.isNullOrEmpty(number)) {
      return "";
    }
    // Strip out the country code if any and add ' to make it a string literal
    int index = number.indexOf("-");
    return "'" + number.substring(index + 1);
  }
}
