package com.dothat.relief.request;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RelayMode;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import com.dothat.relief.request.store.ReliefRequestStore;
import com.dothat.relief.request.task.RequestBroadcastProcessorTaskGenerator;
import com.dothat.relief.request.task.RequestBroadcastTaskGenerator;

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
  
  public Long save(ReliefRequest data, RelayMode relayMode) {
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    if (relayMode == RelayMode.REALTIME) {
      return store.store(data, new RequestBroadcastTaskGenerator());
    } else {
      return store.store(data, new RequestBroadcastProcessorTaskGenerator());
    }
  }
  
  public ReliefRequest lookupRequestById(Long requestId) {
    return store.find(requestId);
  }
  
  public List<ReliefRequest> lookupAllAssigned(
      ObfuscatedID obfuscatedId, ReliefProvider provider, RequestType requestType, int limit) {
    return store.findAll(obfuscatedId.getIdentifier(), provider.getProviderId(), requestType, limit);
  }
  
  public ReliefRequest lookupLastRequest(ObfuscatedID identityUUID, SourceType sourceType, String source,
                                         String rootId, String sourceId) {
    // First see if a request was created on the same call. If so use that.
    List<ReliefRequest> requests = store.findAllForSourceId(identityUUID.getIdentifier(), sourceType, source,
        sourceId, 5);

    // Otherwise find the last request that was created from the same root Id
    if (requests == null || requests.isEmpty()) {
      requests = store.findAllForRootId(identityUUID.getIdentifier(), sourceType, source, rootId, 5);
    }

    // Otherwise find the last request that was created from the same source
    if (requests == null || requests.isEmpty()) {
      // Otherwise find the latest request.
      requests = store.findAllForSource(identityUUID.getIdentifier(), sourceType, source, 5);
    }

    // TODO(abhideep): Eventually, remove the legacy lookup.
    // For Legacy reasons, lookup all Requests for Identity and then sort them.
    if (requests == null || requests.isEmpty()) {
      store.findAllForIdentity(identityUUID.getIdentifier(), 50);
    }

    if (requests == null || requests.isEmpty()) {
      logger.warn("No Request found for {}" , identityUUID.getIdentifier());
      return null;
    }

    requests.sort(new RequestSorter());
    return requests.get(0);
  }
}
