package com.dothat.relief.request.store;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Maintains a list of Unique Request Ids to avoid duplicates.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
class UniqueRequestIdEntity {
  @Id
  private String uniqueId;
  
  private Long requestId;
  
  private UniqueRequestIdEntity() {
    // Empty Constructor for use by Objectify only
  }

  UniqueRequestIdEntity(Long requestId, String uniqueId) {
    this();
    this.requestId = requestId;
    this.uniqueId = uniqueId;
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public Long getRequestId() {
    return requestId;
  }
}
