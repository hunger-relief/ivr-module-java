package com.dothat.relief.provider.data;

/**
 * Data object for the organization that is providing the relief.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefProvider {
  private Long providerId;
  private String providerCode;
  
  public Long getProviderId() {
    return providerId;
  }
  
  public void setProviderId(Long providerId) {
    this.providerId = providerId;
  }
  
  public String getProviderCode() {
    return providerCode;
  }
  
  public void setProviderCode(String providerCode) {
    this.providerCode = providerCode;
  }
}
