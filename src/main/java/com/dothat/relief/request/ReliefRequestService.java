package com.dothat.relief.request;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.SourceType;
import com.dothat.relief.request.store.ReliefRequestStore;
import com.dothat.relief.request.task.RequestBroadcastTaskGenerator;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Service Layer to manage Relief Requests.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefRequestService {
  private final ReliefRequestStore store = new ReliefRequestStore();
  
  public Long save(ReliefRequest data) {
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    return store.store(data, new RequestBroadcastTaskGenerator());
  }
  
  public ReliefRequest lookupRequestById(Long requestId) {
    return store.find(requestId);
  }
  
  public List<ReliefRequest> lookupAllForIdentity(ObfuscatedID obfuscatedId, int limit) {
    return store.findAll(obfuscatedId.getIdentifier(), limit);
  }
  
  public List<ReliefRequest> lookupBySource(ObfuscatedID identityUUID, SourceType sourceType, String source) {
    return store.findAll(identityUUID.getIdentifier(), sourceType, source, 5);
  }
}
