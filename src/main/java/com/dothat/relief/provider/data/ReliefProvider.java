package com.dothat.relief.provider.data;

import com.google.api.server.spi.types.DateAndTime;

/**
 * Data object for the organization that is providing the relief.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefProvider {
  private Long providerId;
  private String providerCode;

  private ProviderConfig config;
  
  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
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
  
  public ProviderConfig getConfig() {
    return config;
  }
  
  public void setConfig(ProviderConfig config) {
    this.config = config;
  }
  
  public DateAndTime getCreationTimestamp() {
    return creationTimestamp;
  }
  
  public void setCreationTimestamp(DateAndTime creationTimestamp) {
    this.creationTimestamp = creationTimestamp;
  }
  
  public DateAndTime getModificationTimestamp() {
    return modificationTimestamp;
  }
  
  public void setModificationTimestamp(DateAndTime modificationTimestamp) {
    this.modificationTimestamp = modificationTimestamp;
  }
}
