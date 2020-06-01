package com.dothat.relief.request;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import com.dothat.relief.request.store.ReliefRequestStore;
import com.dothat.relief.request.task.RequestBroadcastProcessorTaskGenerator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service Layer to manage Relief Requests.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefRequestService {
  private static final Logger logger = LoggerFactory.getLogger(ReliefRequestService.class);
  
  private final ReliefRequestStore store = new ReliefRequestStore();
  
  public Long save(ReliefRequest data) {
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    return store.store(data, new RequestBroadcastProcessorTaskGenerator());
  }
  
  public ReliefRequest lookupRequestById(Long requestId) {
    return store.find(requestId);
  }
  
  public List<ReliefRequest> lookupAllForIdentity(ObfuscatedID obfuscatedId, int limit) {
    return store.findAll(obfuscatedId.getIdentifier(), limit);
  }
  
  public List<ReliefRequest> lookupAllAssigned(
      ObfuscatedID obfuscatedId, ReliefProvider provider, RequestType requestType, int limit) {
    return store.findAll(obfuscatedId.getIdentifier(), provider.getProviderId(), requestType, limit);
  }
  
  public List<ReliefRequest> lookupBySource(ObfuscatedID identityUUID, SourceType sourceType, String source) {
    return store.findAll(identityUUID.getIdentifier(), sourceType, source, 5);
  }
  
  public ReliefRequest lookupLastRequest(ObfuscatedID identityUUID, SourceType sourceType, String source) {
    // TODO(abhideep): Eventually, just use this call.
    List<ReliefRequest> requests = lookupBySource(identityUUID, sourceType, source);
    
    // For Legacy reasons, lookup all Requests for Identity and then sort them.
    if (requests == null || requests.isEmpty()) {
      requests = lookupAllForIdentity(identityUUID, 50);
    }

    if (requests == null || requests.isEmpty()) {
      logger.warn("No Request found for {}" , identityUUID.getIdentifier());
      return null;
    }

    requests.sort(new RequestSorter());
    return requests.get(0);
  }
}
