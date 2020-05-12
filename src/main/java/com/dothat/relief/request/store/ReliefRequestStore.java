package com.dothat.relief.request.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.common.queue.TaskGenerator;
import com.dothat.location.store.LocationStore;
import com.dothat.relief.request.data.ReliefRequest;
import com.googlecode.objectify.Key;

/**
 * Objectify based Store to manage Relief Request Data.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefRequestStore {
  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(ReliefRequestEntity.class);
  
    // Initialize Dependencies
    LocationStore.init();
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
  }
  
  public Long store(ReliefRequest data, TaskGenerator<ReliefRequest> taskGenerator) {
    return PersistenceService.service().transact(() -> {
    
      // Save the data
      ReliefRequestEntity request = new ReliefRequestEntity(data);
      Key<ReliefRequestEntity> key = PersistenceService.service().save().entity(request).now();
    
      // Extract the Request Id
      Long requestId = key.getId();
    
      // Set the Request Id on the Request for new Requests
      data.setRequestId(requestId);
    
      // If there is a task generator, then generate the task.
      if (taskGenerator != null) {
        taskGenerator.generateTask(data);
      }
      return requestId;
    });
  }
  
  public ReliefRequest find(Long requestId) {
    ReliefRequestEntity request = PersistenceService.service().load()
        .type(ReliefRequestEntity.class)
        .id(requestId)
        .now();
    
    if (request == null) {
      return null;
    }
    return request.getData();
  }
}
