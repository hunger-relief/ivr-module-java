package com.dothat.identity.store;

import com.dothat.identity.data.IdSourceType;
import com.dothat.objectify.PersistenceService;

/**
 * Checks Uniqueness constraint for External Source Id.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class UniqueExternalIdConstraint {

  void check(IdSourceType sourceType, String externalId) {
    UniqueExternalIdEntity unique = getUniqueSourceIdEntity(sourceType, externalId);
    if (unique != null) {
      throw new IllegalStateException("An entry exists for " + sourceType + " with Id " + externalId);
    }
  }
  
  void store(Long sourceId, IdSourceType sourceType, String externalId) {
    UniqueExternalIdEntity unique = new UniqueExternalIdEntity(sourceId, generateID(sourceType, externalId));
    PersistenceService.service().save().entity(unique).now();
  }
  
  private String generateID(IdSourceType sourceType, String externalId) {
    return sourceType.name() + "|" + externalId;
  }

  private UniqueExternalIdEntity getUniqueSourceIdEntity(IdSourceType sourceType, String sourceId) {
    return PersistenceService.service().load()
        .type(UniqueExternalIdEntity.class)
        .id(generateID(sourceType, sourceId))
        .now();
  }
  
}
