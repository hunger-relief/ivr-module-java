package com.dothat.profile.store;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Maintains a list of Unique Request Ids to avoid duplicates.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
class UniqueAttributeEntity {
  @Id
  private String uniqueId;
  
  private Long attributeId;
  
  private UniqueAttributeEntity() {
    // Empty Constructor for use by Objectify only
  }

  UniqueAttributeEntity(Long attributeId, String uniqueId) {
    this();
    this.attributeId = attributeId;
    this.uniqueId = uniqueId;
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public Long getAttributeId() {
    return attributeId;
  }
}
