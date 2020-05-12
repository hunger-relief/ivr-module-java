package com.dothat.relief.provider.data;

/**
 * Configuration Data for a Relief Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProviderConfig {
  private String providerCode;
  private String googleSheetId;
  
  public String getProviderCode() {
    return providerCode;
  }
  
  public void setProviderCode(String providerCode) {
    this.providerCode = providerCode;
  }
  
  public String getGoogleSheetId() {
    return googleSheetId;
  }
  
  public void setGoogleSheetId(String googleSheetId) {
    this.googleSheetId = googleSheetId;
  }
}
