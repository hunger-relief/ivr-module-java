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

  ExternalIdEntity(ExternalID data) {
    localId = data.getLocalId();
    externalId = data.getExternalId();
    sourceType = data.getSourceType();
    if (data.getObfuscatedId() != null) {
      obfuscatedId = Ref.create(Key.create(ObfuscatedIdEntity.class, data.getObfuscatedId()));
    }
  }
  
  ObfuscatedID getObfuscatedId() {
    return obfuscatedId.get().getData();
  }
}
