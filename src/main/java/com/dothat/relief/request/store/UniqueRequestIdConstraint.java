package com.dothat.relief.request.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.relief.request.data.SourceType;

/**
 * Checks Uniqueness constraint for External Source Id.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class UniqueRequestIdConstraint {

  Long check(SourceType sourceType, String source, String sourceId) {
    UniqueRequestIdEntity unique = getUniqueRequestIdEntity(sourceType, source, sourceId);
    if (unique != null) {
      return unique.getRequestId();
    }
    return null;
  }
  
  void store(Long requestId, SourceType sourceType, String source, String sourceId) {
    UniqueRequestIdEntity unique = new UniqueRequestIdEntity(requestId, generateID(sourceType, source, sourceId));
    PersistenceService.service().save().entity(unique).now();
  }
  
  private String generateID(SourceType sourceType, String source, String sourceId) {
    return sourceType.name() + "|" + source + "|" + sourceId;
  }

  private UniqueRequestIdEntity getUniqueRequestIdEntity(SourceType sourceType, String source, String sourceId) {
    return PersistenceService.service().load()
        .type(UniqueRequestIdEntity.class)
        .id(generateID(sourceType, source, sourceId))
        .now();
  }
  
}
