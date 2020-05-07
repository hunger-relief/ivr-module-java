package com.dothat.identity.store;

import com.dothat.identity.data.ObfuscatedID;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Entity used to store an Obfuscated Identity.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
class ObfuscatedIdEntity {
  @Id
  String identifier;
  
  ObfuscatedIdEntity(ObfuscatedID data) {
    this.identifier = data.getIdentifier();
  }
  
  ObfuscatedID getData() {
    ObfuscatedID data = new ObfuscatedID();
    data.setIdentifier(identifier);
    return data;
  }
}
