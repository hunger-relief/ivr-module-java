package com.dothat.sync.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.common.queue.TaskGenerator;
import com.dothat.location.store.LocationStore;
import com.dothat.profile.store.ProfileStore;
import com.dothat.relief.provider.store.ProviderStore;
import com.dothat.relief.request.store.ReliefRequestStore;
import com.dothat.sync.data.SyncProfileTask;
import com.dothat.sync.data.SyncRequestTask;
import com.dothat.sync.destination.store.DestinationStore;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Objectify based Store to manage Sync RequestS.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncRequestStore {
  private static final Logger logger = LoggerFactory.getLogger(SyncRequestStore.class);

  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(SyncRequestTaskEntity.class);
    PersistenceService.factory().register(SyncProfileTaskEntity.class);
  
    // Initialize Dependencies
    LocationStore.init();
    ProviderStore.init();
    ProfileStore.init();
    ReliefRequestStore.init();
    DestinationStore.init();
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
  }
  
  public Long storeRequestTask(SyncRequestTask data, TaskGenerator<SyncRequestTask> taskGenerator) {
    return PersistenceService.service().transact(() -> {
      // Save the data
      SyncRequestTaskEntity request = new SyncRequestTaskEntity(data);
      Key<SyncRequestTaskEntity> key = PersistenceService.service().save().entity(request).now();
    
      // Extract the Request Id
      Long taskId = key.getId();
    
      // Set the Task Id on the Request for new Requests
      data.setTaskId(taskId);
    
      // If there is a task generator, then generate the task.
      if (taskGenerator != null) {
        logger.info("Add a Task to Sync Request to a Google Sheet");
        taskGenerator.generateTask(data);
      }
      return taskId;
    });
  }
  
  public Long storeProfileTask(SyncProfileTask data, TaskGenerator<SyncProfileTask> taskGenerator) {
    return PersistenceService.service().transact(() -> {
      // Save the data
      SyncProfileTaskEntity request = new SyncProfileTaskEntity(data);
      Key<SyncProfileTaskEntity> key = PersistenceService.service().save().entity(request).now();
      
      // Extract the Task Id
      Long taskId = key.getId();
      
      // Set the Task Id on the Request for new Requests
      data.setTaskId(taskId);
      
      // If there is a task generator, then generate the task.
      if (taskGenerator != null) {
        logger.info("Add a Task to Sync Profile to a Google Sheet");
        taskGenerator.generateTask(data);
      }
      return taskId;
    });
  }
  
  public List<SyncRequestTask> findRequestTasks(String taskName) {
    List<SyncRequestTaskEntity> requestList = PersistenceService.service().load()
        .type(SyncRequestTaskEntity.class)
        .filter("processTaskName", taskName)
        .list();
  
    if (requestList == null) {
      return null;
    }
    List<SyncRequestTask> dataList = new ArrayList<>();
    for (SyncRequestTaskEntity entity : requestList) {
      dataList.add(entity.getData());
    }
    return dataList;
  }
  
  public List<SyncProfileTask> findProfileTasks(String taskName) {
    List<SyncProfileTaskEntity> requestList = PersistenceService.service().load()
        .type(SyncProfileTaskEntity.class)
        .filter("processTaskName", taskName)
        .list();
    
    if (requestList == null) {
      return null;
    }
    List<SyncProfileTask> dataList = new ArrayList<>();
    for (SyncProfileTaskEntity entity : requestList) {
      dataList.add(entity.getData());
    }
    return dataList;
  }

}
