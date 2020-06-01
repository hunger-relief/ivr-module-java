package com.dothat.sync;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.location.LocationDisplayUtils;
import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.data.SyncProcessType;
import com.dothat.sync.data.SyncProfileTask;
import com.dothat.sync.data.SyncRequestTask;
import com.dothat.sync.destination.DestinationService;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.destination.data.DestinationType;
import com.dothat.sync.store.SyncRequestStore;
import com.dothat.sync.taskgen.SyncProfileProcessorTaskGenerator;
import com.dothat.sync.taskgen.SyncRequestProcessorTaskGenerator;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service Layer to Manage the sync process.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncService {
  private static final Logger logger = LoggerFactory.getLogger(SyncService.class);
  
  private static final String TASK_NAME_SEPARATOR = "-";
  
  private final SyncRequestStore store = new SyncRequestStore();
  
  public Long createRequestSyncTask(ReliefRequest request) {
  
    // Load the Provider if needed
    ReliefProvider provider = request.getProvider();
    if (provider == null) {
      throw new IllegalStateException("Request Cannot be processed for Sync with a Provider assigned");
    }
    if (provider.getProviderId() == null && provider.getProviderCode() != null) {
      provider = new ReliefProviderService().lookupByCode(provider.getProviderCode());
    }
  
    ReliefRequestService service = new ReliefRequestService();
    List<ReliefRequest> requestList = service.lookupAllAssigned(
        request.getRequesterID(), request.getProvider(), request.getRequestType(), 10);
    
    // If there is another request for the date other than this one, skip the request.
    if (hasPreviousRequestForDate(request, requestList)) {
      return null;
    }
  
    Destination destination = new DestinationService()
        .lookupDestination(provider, request.getRequestType(), request.getLocation(),
            DestinationType.GOOGLE_SHEETS);
    if (destination == null || Strings.isNullOrEmpty(destination.getGoogleSheetId())) {
      throw new IllegalStateException("No destination defined for " + request.getRequestType() + " Request "
          + " assigned to " + request.getProvider().getProviderCode()
          + " for location " + LocationDisplayUtils.forLog(request.getLocation())
          + "[ID " + LocationDisplayUtils.idForLog(request.getLocation()) + "]");
    }
    
    SyncRequestTask data = new SyncRequestTask();
    data.setProcessType(SyncProcessType.REQUEST);
    data.setProcessTaskName(getProcessTaskName(SyncProcessType.REQUEST, provider,
        request.getRequestType(), destination));
    data.setProvider(provider);
    data.setRequestType(request.getRequestType());
    data.setDestination(destination);
    data.setReliefRequest(request);
  
    // Otherwise queue the Request for Sync
    return store.storeRequestTask(data, new SyncRequestProcessorTaskGenerator(data.getProcessTaskName()));
  }
  
  private String getProcessTaskName(SyncProcessType processType, ReliefProvider provider,
                                    RequestType requestType, Destination destination) {
    return provider.getProviderCode()
        + TASK_NAME_SEPARATOR + processType.name()
        + TASK_NAME_SEPARATOR + requestType
        + TASK_NAME_SEPARATOR + destination.getGoogleSheetId();
  }
  
  private boolean hasPreviousRequestForDate(ReliefRequest current, List<ReliefRequest> requestList) {
    if (requestList == null || requestList.isEmpty()) {
      return false;
    }
    for (ReliefRequest request : requestList) {
      if (request.getRequestId().equals(current.getRequestId())) {
        continue;
      }
      DateTime currentTimestamp = JodaUtils.toDateTime(current.getRequestTimestamp());
      DateTime requestTimestamp = JodaUtils.toDateTime(request.getRequestTimestamp());
      if (requestTimestamp == null) {
        requestTimestamp = JodaUtils.toDateTime(request.getCreationTimestamp());
      }
      if (currentTimestamp.toDate().equals(requestTimestamp.toDate())
          && requestTimestamp.isBefore(currentTimestamp)) {
        return true;
      }
    }
    return false;
  }
  
  public List<SyncRequestTask> getRequestTasks(String taskName) {
    List<SyncRequestTask> tasks = store.findRequestTasks(taskName);
    if (tasks == null || tasks.isEmpty()) {
      return null;
    }
    return tasks;
  }
  
  public void deleteRequestTasks(List<SyncRequestTask> taskList) {
    store.deleteRequestTasks(taskList);
  }
  
  public Long createSyncProfileTask(ProfileAttribute attribute) {
    ProfileService service = new ProfileService();
    List<ProfileAttribute> attributes = service.lookupAllBySourceId(
        attribute.getIdentityUUID(), attribute.getSourceType(), attribute.getSource(), attribute.getSourceId());
  
    // Not sure how this is even possible, but just a fail-safe to make it easier to write code ahead.
    if (attributes == null || attributes.isEmpty()) {
      throw new IllegalStateException("No Attributes found for ID " + attribute.getSourceId() + " from "
          + attribute.getSourceType() + " " + attribute.getSource());
    }
    logger.info("Found {} attributes with source {} and source Id {} for source type {}",
        attributes.size(), attribute.getSource(), attribute.getSourceId(), attribute.getSourceType());
  
    if (!isLatestAttribute(attribute, attributes)) {
      logger.info("Not Processing Attribute for {} from {} {} with Source Id {}",
          attribute.getIdentityUUID().getIdentifier(), attribute.getSourceType(), attribute.getSource(),
          attribute.getSourceId());
      return null;
    }
    logger.info("Processing Last Attribute for {} from {} {} with Source Id {}",
        attribute.getIdentityUUID().getIdentifier(), attribute.getSourceType(), attribute.getSource(),
        attribute.getSourceId());

    // TODO(abhideep): Add support for Multiple Requests
    ReliefRequest request = new ReliefRequestService().lookupLastRequest(
        attribute.getIdentityUUID(), attribute.getSourceType(), attribute.getSource());

    if (request == null) {
      logger.error("No Request found for {} from {} {} ",
          attribute.getIdentityUUID().getIdentifier(), attribute.getSourceType(), attribute.getSource());
      throw new IllegalStateException("No Request found for "
          +  attribute.getIdentityUUID().getIdentifier()
          + " from " + attribute.getSourceType() + " " + attribute.getSource());
    }
  
    Destination destination = new DestinationService()
        .lookupDestination(request.getProvider(), request.getRequestType(), request.getLocation(),
            DestinationType.GOOGLE_SHEETS);
    if (destination == null || Strings.isNullOrEmpty(destination.getGoogleSheetId())) {
      throw new IllegalStateException("No destination defined for " + request.getRequestType() + " Request "
          + " assigned to " + request.getProvider().getProviderCode()
          + " for location " + LocationDisplayUtils.forLog(request.getLocation())
          + "[ID " + LocationDisplayUtils.idForLog(request.getLocation()) + "]");
    }
  
    SyncProfileTask data = new SyncProfileTask();
    data.setProcessType(SyncProcessType.PROFILE);
    data.setProcessTaskName(getProcessTaskName(SyncProcessType.PROFILE, request.getProvider(),
        request.getRequestType(), destination));
    data.setProvider(request.getProvider());
    data.setRequestType(request.getRequestType());
    data.setDestination(destination);
    data.setProfileAttribute(attribute);
  
    // Otherwise queue the Request for Sync
    return store.storeProfileTask(data, new SyncProfileProcessorTaskGenerator(data.getProcessTaskName()));
  }
  
  private boolean isLatestAttribute(ProfileAttribute attribute, List<ProfileAttribute> attrList) {
    if (attrList == null || attrList.isEmpty()) {
      return true;
    }
    DateTime attributeTimestamp = JodaUtils.toDateTime(attribute.getTimestamp());
    for (ProfileAttribute attr : attrList) {
      DateTime attrTimestamp = JodaUtils.toDateTime(attr.getTimestamp());
      if (!attr.getAttributeId().equals(attribute.getAttributeId())
          && attrTimestamp.isAfter(attributeTimestamp)) {
        return false;
      }
    }
    return true;
  }
  
  public List<SyncProfileTask> getProfileTasks(String taskName) {
    List<SyncProfileTask> tasks = store.findProfileTasks(taskName);
    if (tasks == null || tasks.isEmpty()) {
      return null;
    }
    return tasks;
  }
  
  public void deleteProfileTasks(List<SyncProfileTask> taskList) {
    store.deleteProfileTasks(taskList);
  }
}