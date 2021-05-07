package com.dothat.relief.request.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.common.queue.TaskGenerator;
import com.dothat.location.store.LocationStore;
import com.dothat.relief.provider.store.ProviderStore;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import com.googlecode.objectify.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Objectify based Store to manage Relief Request Data.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefRequestStore {
  private static final Logger logger = LoggerFactory.getLogger(ReliefRequestStore.class);

  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(ReliefRequestEntity.class);
    PersistenceService.factory().register(UniqueRequestIdEntity.class);
  
    // Initialize Dependencies
    LocationStore.init();
    ProviderStore.init();
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
  }
  
  public Long store(ReliefRequest data, TaskGenerator<ReliefRequest> taskGenerator) {
    return PersistenceService.service().transact(() -> {
      // Create and check the constraint first
      UniqueRequestIdConstraint constraint = new UniqueRequestIdConstraint();
      Long currentId = constraint.check(data.getSourceType(), data.getSource(), data.getSourceRootId(),
              data.getSourceId());
      if (currentId != null) {
        logger.warn("Multiple requests received for creating a Request with for Notification with " +
                "source Id {} from {} {} and Root {}", data.getSourceId(), data.getSourceType(),data.getSource(),
                data.getSourceRootId());
        return currentId;
      }
    
      // Save the data
      ReliefRequestEntity request = new ReliefRequestEntity(data);
      Key<ReliefRequestEntity> key = PersistenceService.service().save().entity(request).now();
    
      // Extract the Request Id
      Long requestId = key.getId();
    
      // Set the Request Id on the Request for new Requests
      data.setRequestId(requestId);
    
      // Save the constraint to avoid future conflicts
      constraint.store(data.getRequestId(), data.getSourceType(), data.getSource(), data.getSourceRootId(),
              data.getSourceId());

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
  
  public List<ReliefRequest> findAll(String obfuscatedId, Long providerId, RequestType requestType, int limit) {
    List<ReliefRequestEntity> requestList = PersistenceService.service().load()
        .type(ReliefRequestEntity.class)
        .filter("requesterUUID", obfuscatedId)
        .filter("providerId ", providerId)
        .filter("requestType ", requestType)
        .order("-requestTimestamp")
        .limit(limit)
        .list();

    return toDataList(requestList);
  }
  
  public List<ReliefRequest> findAllForIdentity(String obfuscatedId, int limit) {
    List<ReliefRequestEntity> requestList = PersistenceService.service().load()
        .type(ReliefRequestEntity.class)
        .filter("requesterUUID", obfuscatedId)
        .order("-requestTimestamp")
        .limit(limit)
        .list();

    return toDataList(requestList);
  }
  
  public List<ReliefRequest> findAllForSource(String obfuscatedId, SourceType sourceType, String source, int limit) {
    List<ReliefRequestEntity> requestList = PersistenceService.service().load()
        .type(ReliefRequestEntity.class)
        .filter("requesterUUID", obfuscatedId)
        .filter("sourceType", sourceType)
        .filter("source", source)
        .order("-requestTimestamp")
        .limit(limit)
        .list();

    return toDataList(requestList);
  }

  public List<ReliefRequest> findAllForRootId(String obfuscatedId, SourceType sourceType, String source,
                                              String sourceRootId, int limit) {
    List<ReliefRequestEntity> requestList = PersistenceService.service().load()
        .type(ReliefRequestEntity.class)
        .filter("requesterUUID", obfuscatedId)
        .filter("sourceType", sourceType)
        .filter("source", source)
        .filter("sourceRootId", sourceRootId)
        .order("-requestTimestamp")
        .limit(limit)
        .list();

    return toDataList(requestList);
  }

  public List<ReliefRequest> findAllForSourceId(String obfuscatedId, SourceType sourceType, String source,
                                                String sourceId, int limit) {
    List<ReliefRequestEntity> requestList = PersistenceService.service().load()
        .type(ReliefRequestEntity.class)
        .filter("requesterUUID", obfuscatedId)
        .filter("sourceType", sourceType)
        .filter("source", source)
        .filter("sourceId", sourceId)
        .order("-requestTimestamp")
        .limit(limit)
        .list();

    return toDataList(requestList);
  }

  private List<ReliefRequest> toDataList(List<ReliefRequestEntity> requestList) {
    if (requestList == null) {
      return null;
    }
    List<ReliefRequest> dataList = new ArrayList<>();
    for (ReliefRequestEntity entity : requestList) {
      dataList.add(entity.getData());
    }
    return dataList;
  }
}
