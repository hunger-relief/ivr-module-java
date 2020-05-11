package com.dothat.ivr.notif.store;

import com.dothat.common.queue.TaskGenerator;
import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.objectify.PersistenceService;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Stores the data for an IVR Call.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRCallStore {
  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(IVRCallEntity.class);
    PersistenceService.factory().register(IVRCallNodeEntity.class);
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
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
  
  public Long store(IVRCallNode data, TaskGenerator<IVRCallNode> taskGenerator) {
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    
    return PersistenceService.service().transact(() -> {
      
      // Save the data
      IVRCallNodeEntity node = new IVRCallNodeEntity(data);
      Key<IVRCallNodeEntity> key = PersistenceService.service().save().entity(node).now();
      
      // Extract the Node Id
      Long nodeId = key.getId();
      
      // Set the Call Id on the Call Data for new Calls
      data.setCallNodeId(nodeId);
      
      // If there is a task generator, then generate the task.
      if (taskGenerator != null) {
        taskGenerator.generateTask(data);
      }
      return nodeId;
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
  
  public IVRCallNode findNode(Long callNodeId) {
    IVRCallNodeEntity node = PersistenceService.service().load()
        .type(IVRCallNodeEntity.class)
        .id(callNodeId)
        .now();
    
    if (node == null) {
      return null;
    }
    return node.getData();
  }
  
  public IVRCall find(String providerCallId) {
    List<IVRCallEntity> calls = PersistenceService.service().load()
        .type(IVRCallEntity.class)
        .filter("providerCallId", providerCallId)
        .list();
    
    if (calls == null || calls.isEmpty()) {
      return null;
    }
    // Just return the fist entry
    return calls.get(0).getData();
  }
  
}
