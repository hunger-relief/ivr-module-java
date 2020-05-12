package com.dothat.profile.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.common.queue.TaskGenerator;
import com.dothat.profile.data.ProfileAttribute;
import com.googlecode.objectify.Key;

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
    ProfileAttributeEntity request = PersistenceService.service().load()
        .type(ProfileAttributeEntity.class)
        .id(attributeId)
        .now();
    
    if (request == null) {
      return null;
    }
    return request.getData();
  }
}
