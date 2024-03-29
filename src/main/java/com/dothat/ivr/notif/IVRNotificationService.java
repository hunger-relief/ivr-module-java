package com.dothat.ivr.notif;

import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.ivr.notif.data.IVRProvider;
import com.dothat.ivr.notif.data.ParseStatus;
import com.dothat.ivr.notif.store.IVRCallStore;
import com.dothat.ivr.notif.task.IVRCallNodeProcessorTaskGenerator;
import com.dothat.ivr.notif.task.IVRCallProcessorTaskGenerator;

/**
 * Service layer for IVR based Services.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRNotificationService {
  private final IVRCallStore store = new IVRCallStore();
  
  public Long saveCall(IVRCall data) {
    if (ParseStatus.SUCCESS.equals(data.getParseStatus())) {
      return store.store(data, new IVRCallProcessorTaskGenerator());
    }
    return store.store(data, null);
  }

  public IVRCall lookupCallById(Long callId) {
    return store.find(callId);
  }
  
  public IVRCall lookupCallByProviderId(String providerCallId) {
    return store.find(providerCallId);
  }

  public IVRCallNode lookupNodeByProviderId(IVRProvider provider, String providerCallId) {
    return store.findNode(provider, providerCallId);
  }
  
  public Long saveCallNode(IVRCallNode node) {
    if (ParseStatus.SUCCESS.equals(node.getParseStatus())) {
      return store.store(node, new IVRCallNodeProcessorTaskGenerator());
    }
    return store.store(node, null);
  }
  
  public IVRCallNode lookupNodeById(Long callNodeId) {
    return store.findNode(callNodeId);
  }
}
