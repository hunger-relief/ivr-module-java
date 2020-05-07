package com.dothat.identity.data;

/**
 * Data object for an Id sourced from an External System that becomes the basis for the Obfuscated Id.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ExternalID {
  private Long localId; // Local Database Id
  
  private String obfuscatedId; // Unique Obfuscated Id

  private String externalId; // External Id that is unique within the Source Type
  private IdSourceType sourceType; // Type of External Id Source
  
  public Long getLocalId() {
    return localId;
  }
  
  public void setLocalId(Long localId) {
    this.localId = localId;
  }
  
  public String getObfuscatedId() {
    return obfuscatedId;
  }
  
  public void setObfuscatedId(String obfuscatedId) {
    this.obfuscatedId = obfuscatedId;
  }
  
  public String getExternalId() {
    return externalId;
  }
  
  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }
  
  public IdSourceType getSourceType() {
    return sourceType;
  }
  
  public void setSourceType(IdSourceType sourceType) {
    this.sourceType = sourceType;
  }
}
