package com.dothat.identity.store;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Maintains a list of Unique Ids to avoid duplicates in Source Identifiers.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
class UniqueExternalIdEntity {
  @Id
  String uniqueId;

  Long sourceId;
  
  private UniqueExternalIdEntity() {
    // Empty Constructor for use by Objectify only
  }

  UniqueExternalIdEntity(Long sourceId, String uniqueId) {
    this();
    this.uniqueId = uniqueId;
    this.sourceId = sourceId;
  }
}
