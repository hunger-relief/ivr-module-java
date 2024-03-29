package com.dothat.identity.store;

import com.dothat.identity.data.ObfuscatedID;
import com.dothat.identity.data.IdSourceType;
import com.dothat.identity.data.ExternalID;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

/**
 * Entity used to store an Obfuscated Identity.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
class ExternalIdEntity {
  @Id
  Long localId;

  @Parent @Load
  Ref<ObfuscatedIdEntity> obfuscatedId;

  @Index
  String externalId;
  
  @Index
  IdSourceType sourceType;
  
  private ExternalIdEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  ExternalIdEntity(ExternalID data) {
    this();
    localId = data.getLocalId();
    externalId = data.getExternalId();
    sourceType = data.getSourceType();
    if (data.getObfuscatedId() != null) {
      obfuscatedId = Ref.create(Key.create(ObfuscatedIdEntity.class, data.getObfuscatedId()));
    }
  }
  
  ExternalID getData() {
    ExternalID data = new ExternalID();
    data.setLocalId(localId);
    data.setExternalId(externalId);
    data.setSourceType(sourceType);
    ObfuscatedID id = getObfuscatedId();
    if (id != null) {
      data.setObfuscatedId(id.getIdentifier());
    }
    return data;
  }
  
  ObfuscatedID getObfuscatedId() {
    return obfuscatedId.get().getData();
  }
}
