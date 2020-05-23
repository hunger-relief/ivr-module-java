package com.dothat.relief.request;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.SourceType;
import com.dothat.relief.request.store.ReliefRequestStore;
import com.dothat.relief.request.task.RequestBroadcastTaskGenerator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
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
  
  private static class RequestSorter implements Comparator<ReliefRequest> {
  
    @Override
    public int compare(ReliefRequest lhsRequest, ReliefRequest rhsRequest) {
      DateTime lhs = JodaUtils.toDateTime(lhsRequest.getRequestTimestamp());
      DateTime rhs = JodaUtils.toDateTime(rhsRequest.getRequestTimestamp());
      
      if (lhs != null && rhs != null) {
        return lhs.compareTo(rhs);
      } else if (lhs == null && rhs == null) {
        return JodaUtils.toDateTime(lhsRequest.getCreationTimestamp())
            .compareTo(JodaUtils.toDateTime(rhsRequest.getCreationTimestamp()));
      } else if (lhs != null) {
        return 1;
      }
      return -1;
    }
  }
}
