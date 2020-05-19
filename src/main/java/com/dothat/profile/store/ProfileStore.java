package com.dothat.profile.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.common.queue.TaskGenerator;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.request.data.SourceType;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Objectify based Store to manage Relief Request Data.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileStore {
  private static final Logger logger = LoggerFactory.getLogger(ProfileStore.class);

  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(ProfileAttributeEntity.class);
    PersistenceService.factory().register(UniqueAttributeEntity.class);
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
  }
  
  public Long store(ProfileAttribute data, TaskGenerator<ProfileAttribute> taskGenerator) {
    return PersistenceService.service().transact(() -> {
      // Create and check the constraint first
      UniqueAttributeConstraint constraint = new UniqueAttributeConstraint();
      Long currentId = constraint.check(data.getSourceType(), data.getSource(), data.getSourceId(),
          data.getAttributeName());
      if (currentId != null) {
        logger.warn("Multiple requests received for creating Attribute " + data.getAttributeName()
            + " with source Id " + data.getSourceId() + " from " + data.getSourceType() + " "
            + data.getSource());
        return currentId;
      }
    
      // Save the data
      ProfileAttributeEntity attribute = new ProfileAttributeEntity(data);
      Key<ProfileAttributeEntity> key = PersistenceService.service().save().entity(attribute).now();
    
      // Extract the Attribute Id
      Long attributeId = key.getId();
    
      // Set the Attribute Id on the Attribute
      data.setAttributeId(attributeId);
    
      // Save the constraint to avoid future conflicts
      constraint.store(data.getAttributeId(), data.getSourceType(), data.getSource(), data.getSourceId(),
          data.getAttributeName());

      // If there is a task generator, then generate the task.
      if (taskGenerator != null) {
        taskGenerator.generateTask(data);
      }
      return attributeId;
    });
  }
  
  public ProfileAttribute find(Long attributeId) {
    ProfileAttributeEntity attribute = PersistenceService.service().load()
        .type(ProfileAttributeEntity.class)
        .id(attributeId)
        .now();
    
    if (attribute == null) {
      return null;
    }
    return attribute.getData();
  }
  
  
  public List<ProfileAttribute> findAllForSource(String obfuscatedId, SourceType sourceType,
                                                 String source,  String sourceId) {
    List<ProfileAttributeEntity> list = PersistenceService.service().load()
        .type(ProfileAttributeEntity.class)
        .filter("identityUUID", obfuscatedId)
        .filter("sourceType", sourceType)
        .filter("source", source)
        .filter("sourceId", sourceId)
        .list();
  
    if (list == null) {
      return null;
    }
    List<ProfileAttribute> dataList = new ArrayList<>();
    for (ProfileAttributeEntity entity : list) {
      dataList.add(entity.getData());
    }
    return dataList;
  }
  
  public List<ProfileAttribute> findAllForName(String obfuscatedId, String sourceId) {
    List<ProfileAttributeEntity> list = PersistenceService.service().load()
        .type(ProfileAttributeEntity.class)
        .filter("attributeName", sourceId)
        .filter("identityUUID", obfuscatedId)
        .list();
    
    if (list == null) {
      return null;
    }
    List<ProfileAttribute> dataList = new ArrayList<>();
    for (ProfileAttributeEntity entity : list) {
      dataList.add(entity.getData());
    }
    return dataList;
  }
  
  public List<ProfileAttribute> findAllForUUID(String obfuscatedId) {
    List<ProfileAttributeEntity> list = PersistenceService.service().load()
        .type(ProfileAttributeEntity.class)
        .filter("identityUUID", obfuscatedId)
        .list();
  
    if (list == null) {
      return null;
    }
    List<ProfileAttribute> dataList = new ArrayList<>();
    for (ProfileAttributeEntity entity : list) {
      dataList.add(entity.getData());
    }
    return dataList;
  }
}
