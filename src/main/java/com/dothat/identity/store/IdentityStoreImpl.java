package com.dothat.identity.store;

import com.dothat.identity.data.IdSourceType;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.identity.data.ExternalID;
import com.dothat.objectify.PersistenceService;
import com.googlecode.objectify.Key;

import java.util.List;

/**
 * Objectify implementation for the IdentityStore
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IdentityStoreImpl implements IdentityStore {
  
  static {
    PersistenceService.factory().register(ObfuscatedIdEntity.class);
    PersistenceService.factory().register(ExternalIdEntity.class);
    PersistenceService.factory().register(UniqueExternalIdEntity.class);
  }
  
  @Override
  public ObfuscatedID load(IdSourceType sourceType, String sourceId) {
    List<ExternalIdEntity> idList = PersistenceService.service().load()
        .type(ExternalIdEntity.class)
        .filter("sourceType", sourceType.name())
        .filter("externalId", sourceId)
        .list();
    if (idList == null || idList.size() == 0) {
      return null;
    } else if (idList.size() == 1) {
      return idList.get(0).getObfuscatedId();
    }
    throw new IllegalStateException("More than one entry found for " + sourceType + " with Id " + sourceId);
  }
  
  public ObfuscatedID store(ExternalID data) {
    return PersistenceService.service().transact(() -> {
      // Create and check the constraint first
      UniqueExternalIdConstraint constraint = new UniqueExternalIdConstraint();
      constraint.check(data.getSourceType(), data.getExternalId());

      // Save the Obfuscated Id first
      ObfuscatedID id = new ObfuscatedID();
      id.setIdentifier(data.getObfuscatedId());
      ObfuscatedIdEntity obfuscatedId = new ObfuscatedIdEntity(id);
      PersistenceService.service().save().entity(obfuscatedId).now();

      // Save the data
      ExternalIdEntity externalId = new ExternalIdEntity(data);
      Key<ExternalIdEntity> key = PersistenceService.service().save().entity(externalId).now();
      data.setLocalId(key.getId());

      // Save the constraint to avoid future conflicts
      constraint.store(data.getLocalId(), data.getSourceType(), data.getExternalId());

      // Return Obfuscated Id
      return id;
    });
  }
}
