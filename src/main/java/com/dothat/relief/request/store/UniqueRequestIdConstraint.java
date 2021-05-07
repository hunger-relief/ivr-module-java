package com.dothat.relief.request.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.relief.request.data.SourceType;

/**
 * Checks Uniqueness constraint for External Source Id.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class UniqueRequestIdConstraint {

  Long check(SourceType sourceType, String source, String sourceRootId, String sourceId) {
    UniqueRequestIdEntity unique = getUniqueRequestIdEntity(sourceType, source, sourceRootId, sourceId);
    if (unique != null) {
      return unique.getRequestId();
    }
    return null;
  }
  
  void store(Long requestId, SourceType sourceType, String source, String sourceRootId, String sourceId) {
    UniqueRequestIdEntity unique = new UniqueRequestIdEntity(requestId,
            generateID(sourceType, source, sourceRootId, sourceId));
    PersistenceService.service().save().entity(unique).now();
  }
  
  private String generateID(SourceType sourceType, String source, String sourceRootId, String sourceId) {
    return sourceType.name() + "|" + source + "|" + sourceRootId + "|" + sourceId;
  }

  private UniqueRequestIdEntity getUniqueRequestIdEntity(SourceType sourceType, String source, String sourceRootId,
                                                         String sourceId) {
    return PersistenceService.service().load()
        .type(UniqueRequestIdEntity.class)
        .id(generateID(sourceType, source, sourceRootId, sourceId))
        .now();
  }
  
}
