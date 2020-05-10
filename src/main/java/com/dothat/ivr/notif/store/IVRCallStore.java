package com.dothat.ivr.notif.store;

import com.dothat.common.queue.TaskGenerator;
import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.objectify.PersistenceService;
import com.dothat.location.store.LocationEntity;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;

/**
 * Stores the data for an IVR Call.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRCallStore {
  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(IVRCallEntity.class);
  }
  
  public Long store(IVRCall data, TaskGenerator<IVRCall> taskGenerator) {
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    
    return PersistenceService.service().transact(() -> {
    
      // Save the data
      IVRCallEntity call = new IVRCallEntity(data);
      Key<IVRCallEntity> key = PersistenceService.service().save().entity(call).now();
    
      // Extract the Call Id
      Long callId = key.getId();
    
      // Set the Call Id on the Call Data for new Calls
      data.setCallId(callId);
      
      // If there is a task generator, then generate the task.
      if (taskGenerator != null) {
        taskGenerator.generateTask(data);
      }
      return callId;
    });
  }
  
  public IVRCall find(Long callId) {
    IVRCallEntity call = PersistenceService.service().load()
        .type(IVRCallEntity.class)
        .id(callId)
        .now();
  
    if (call == null) {
      return null;
    }
    return call.getData();
  }
}
