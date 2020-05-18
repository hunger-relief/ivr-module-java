package com.dothat.profile.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.common.queue.TaskGenerator;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.request.data.SourceType;
import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.List;

/**
 * Objectify based Store to manage Relief Request Data.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileStore {
  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(ProfileAttributeEntity.class);
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
  }
  
  public Long store(ProfileAttribute data, TaskGenerator<ProfileAttribute> taskGenerator) {
    return PersistenceService.service().transact(() -> {
    
      // Save the data
      ProfileAttributeEntity attribute = new ProfileAttributeEntity(data);
      Key<ProfileAttributeEntity> key = PersistenceService.service().save().entity(attribute).now();
    
      // Extract the Attribute Id
      Long attributeId = key.getId();
    
      // Set the Attribute Id on the Attribute
      data.setAttributeId(attributeId);
    
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
}
