package com.dothat.profile.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.request.data.SourceType;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

/**
 * Entity that stores Profile using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class ProfileAttributeEntity {
  @Id
  private Long attributeId;
  
  @Index
  private String identityUUID;
  
  @Index
  private String attributeName;
  private String attributeValue;
  
  @Index
  private String source;
  @Index
  private String sourceId;
  @Index
  private SourceType sourceType;
  
  @Index
  private DateTime timestamp;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;
  
  private ProfileAttributeEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  ProfileAttributeEntity(ProfileAttribute data) {
    this();
    attributeId = data.getAttributeId();
    if (data.getIdentityUUID() != null) {
      identityUUID = data.getIdentityUUID().getIdentifier();
    }
    attributeName = data.getAttributeName();
    attributeValue = data.getAttributeValue();
    
    source = data.getSource();
    sourceId = data.getSourceId();
    sourceType = data.getSourceType();
    
    timestamp = JodaUtils.toDateTime(data.getTimestamp());
    
    if (data.getCreationTimestamp() != null) {
      creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    }
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  }
  
  ProfileAttribute getData() {
    ProfileAttribute data = new ProfileAttribute();
    data.setAttributeId(attributeId);
    if (identityUUID != null) {
      ObfuscatedID id = new ObfuscatedID();
      id.setIdentifier(identityUUID);
      data.setIdentityUUID(id);
    }
    
    data.setAttributeName(attributeName);
    data.setAttributeValue(attributeValue);
    
    data.setSource(source);
    data.setSourceId(sourceId);
    data.setSourceType(sourceType);
  
    data.setTimestamp(JodaUtils.toDateAndTime(timestamp));

    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
  
    return data;
  }
}
