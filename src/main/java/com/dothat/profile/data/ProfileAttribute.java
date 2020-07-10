package com.dothat.profile.data;

import com.dothat.identity.data.ObfuscatedID;
import com.dothat.relief.request.data.SourceType;
import com.google.api.server.spi.types.DateAndTime;

/**
 * Data for An Attribute of the Profile.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileAttribute {
  private Long attributeId;
  private ObfuscatedID identityUUID;
  
  private String attributeName;
  private String attributeValue;

  private SourceType sourceType;
  private String source;
  private String sourceRootId;
  private String sourceId;

  private DateAndTime timestamp;

  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getAttributeId() {
    return attributeId;
  }
  
  public void setAttributeId(Long attributeId) {
    this.attributeId = attributeId;
  }
  
  public ObfuscatedID getIdentityUUID() {
    return identityUUID;
  }
  
  public void setIdentityUUID(ObfuscatedID identityUUID) {
    this.identityUUID = identityUUID;
  }

  public SourceType getSourceType() {
    return sourceType;
  }

  public void setSourceType(SourceType sourceType) {
    this.sourceType = sourceType;
  }

  public String getSource() {
    return source;
  }
  
  public void setSource(String source) {
    this.source = source;
  }

  public String getSourceRootId() {
    return sourceRootId;
  }

  public void setSourceRootId(String sourceRootId) {
    this.sourceRootId = sourceRootId;
  }

  public String getSourceId() {
    return sourceId;
  }
  
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }
  
  public String getAttributeName() {
    return attributeName;
  }
  
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }
  
  public String getAttributeValue() {
    return attributeValue;
  }
  
  public void setAttributeValue(String attributeValue) {
    this.attributeValue = attributeValue;
  }
  
  public DateAndTime getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(DateAndTime timestamp) {
    this.timestamp = timestamp;
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
