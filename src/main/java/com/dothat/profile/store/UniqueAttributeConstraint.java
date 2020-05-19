package com.dothat.profile.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.relief.request.data.SourceType;

/**
 * Checks Uniqueness constraint for External Source Id.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class UniqueAttributeConstraint {

  Long check(SourceType sourceType, String source, String sourceId, String attributeName) {
    UniqueAttributeEntity unique = getUniqueAttributeEntity(sourceType, source, sourceId, attributeName);
    if (unique != null) {
      return unique.getAttributeId();
    }
    return null;
  }
  
  void store(Long requestId, SourceType sourceType, String source, String sourceId, String attributeName) {
    UniqueAttributeEntity unique = new UniqueAttributeEntity(requestId,
        generateID(sourceType, source, sourceId, attributeName));
    PersistenceService.service().save().entity(unique).now();
  }
  
  private String generateID(SourceType sourceType, String source, String sourceId, String attributeName) {
    return sourceType.name() + "|" + source + "|" + sourceId + "|" + attributeName;
  }

  private UniqueAttributeEntity getUniqueAttributeEntity(SourceType sourceType, String source, String sourceId,
                                                        String attributeName) {
    return PersistenceService.service().load()
        .type(UniqueAttributeEntity.class)
        .id(generateID(sourceType, source, sourceId, attributeName))
        .now();
  }
}
