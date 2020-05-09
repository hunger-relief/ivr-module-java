package com.dothat.ivr.store;

import com.dothat.ivr.data.IVRCall;
import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.objectify.PersistenceService;
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
  
  public Long store(IVRCall data) {
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
      return callId;
    });
  }
}
