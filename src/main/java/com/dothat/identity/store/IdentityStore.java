package com.dothat.identity.store;

import com.dothat.identity.data.IdSourceType;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.identity.data.ExternalID;

/**
 * Interface for classes that store Identity information.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface IdentityStore {

  /**
   * Load the Obfuscated Id the given Source Type and Source ID.
   *
   * @param sourceType Type of Source
   * @param sourceId Id provided by the source.
   * @return Obfuscated id associated with the source Id
   */
  ObfuscatedID load(IdSourceType sourceType, String sourceId);
  
  /**
   * Store an obfuscated Id for the given Source ID.
   * @param data Data for the source ID.
   * @return The Obfuscated Id that was created.
   */
  ObfuscatedID store(ExternalID data);
}
