package com.dothat.common.validate.phone;

/**
 * Data for format used by the country.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class PhoneFormat {
  private final int minDigits;
  private final int maxDigits;
  
  public PhoneFormat(int numDigits) {
    this(numDigits, numDigits);
  }
  
  public PhoneFormat(int minDigits, int maxDigits) {
    this.minDigits = minDigits;
    this.maxDigits = maxDigits;
  }
  
  public final int getMinDigits() {
    return minDigits;
  }
  
  public final int getMaxDigits() {
    return maxDigits;
  }
}
