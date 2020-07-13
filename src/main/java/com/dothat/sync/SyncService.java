package com.dothat.sync;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.ivr.mapping.IVRMappingService;
import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.ivr.notif.IVRNotificationService;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.ivr.notif.data.IVRProvider;
import com.dothat.location.LocationDisplayUtils;
import com.dothat.location.data.Location;
import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.provider.ReliefProviderAssigner;
import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestSource;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
  private static final DateTimeFormatter DATE_TIME_FORMATTER =  DateTimeFormat.forPattern("yyyyMMddHHmm");
  
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
    DateTime now = DateTime.now();
    DateTime processTime = getProcessTime(now, provider);
    
    SyncRequestTask data = new SyncRequestTask();
    data.setProcessType(SyncProcessType.REQUEST);
    data.setProcessTaskName(getProcessTaskName(SyncProcessType.REQUEST, provider, destination, processTime));
    data.setProvider(provider);
    data.setRequestType(request.getRequestType());
    data.setDestination(destination);
    data.setReliefRequest(request);
  
    // Otherwise queue the Request for Sync
    return store.storeRequestTask(data, new SyncRequestProcessorTaskGenerator(data.getProcessTaskName(),
        processTime.getMillis() - now.getMillis()));
  }
  
  private String getProcessTaskName(SyncProcessType processType, ReliefProvider provider,
                                    Destination destination, DateTime processTime) {
    return provider.getProviderCode()
        + TASK_NAME_SEPARATOR + processType.name()
        + TASK_NAME_SEPARATOR + destination.getGoogleSheetId()
        + TASK_NAME_SEPARATOR + DATE_TIME_FORMATTER.print(processTime);
  }
  
  private DateTime getProcessTime( DateTime now, ReliefProvider provider) {
    DateTime processTime = now.plusMinutes(5);
    int minuteOffset = processTime.getMinuteOfHour() % 5;
    int providerOffset = new Long(provider.getProviderId() % 5).intValue();
    processTime = processTime
        .minusMinutes(minuteOffset)
        .plusMinutes(providerOffset);
    return processTime;
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
        attribute.getIdentityUUID(), attribute.getSourceType(), attribute.getSource(), attribute.getSourceRootId(),
        attribute.getSourceId());
  
    RequestType requestType = RequestType.UNKNOWN;
    Location location = null;
    ReliefProvider provider = null;
    if (request != null) {
      requestType = request.getRequestType();
      location = request.getLocation();
      provider = request.getProvider();
    } else {
      logger.warn("No Request found for {} from {} {} ",
          attribute.getIdentityUUID().getIdentifier(), attribute.getSourceType(), attribute.getSource());
  
      if (attribute.getSourceType() == SourceType.IVR) {
        IVRProvider ivrProvider =IVRProvider.valueOf(attribute.getSource());
        IVRCallNode call = new IVRNotificationService()
            .lookupNodeByProviderId(ivrProvider, attribute.getSourceId());

        IVRMappingService mappingService = new IVRMappingService();
        IVRMapping mapping = mappingService.lookup(call.getDialedNumber(), null, false);
        if (mapping != null) {
          requestType = mapping.getRequestType();
          location = mapping.getLocation();
        }
  
        logger.warn("Using the Dialed Number {} and IVRNumber {} Found IVR Mapping "
                + "for Request Type {} and Location {}", call.getDialedNumber(), call.getIvrNumber(),
            requestType, LocationDisplayUtils.forLog(location));
  
        // Assign it to a Provider based on the Phone Number
        ObfuscatedID obfId = attribute.getIdentityUUID();
        RequestSource source = new RequestSource();
        source.setSourceType(attribute.getSourceType());
        source.setSource(attribute.getSource());
        source.setSourceId(attribute.getSourceId());
  
        source.setDialedNumber(call.getDialedNumber());
        source.setCountry(call.getCountry());
  
        provider = new ReliefProviderAssigner().assignProvider(
            obfId, source, requestType, location);
      } else if (attribute.getSourceType() == SourceType.PROVIDER) {
        String providerCode = attribute.getSource();
        provider = new ReliefProviderService().lookupByCode(providerCode);
      }
    }

    if (provider == null) {
      throw new IllegalStateException("No Provider found for "
          + attribute.getIdentityUUID().getIdentifier()
          + " from " + attribute.getSourceType() + " " + attribute.getSource());
    }
  
    if (provider.getProviderId() == null
        && !Strings.isNullOrEmpty(provider.getProviderCode())) {
      provider = new ReliefProviderService().lookupByCode(provider.getProviderCode());
    }
  
    Destination destination = new DestinationService()
        .lookupDestination(provider, requestType, location, DestinationType.GOOGLE_SHEETS);
    if (destination == null || Strings.isNullOrEmpty(destination.getGoogleSheetId())) {
      throw new IllegalStateException("No destination defined for " + requestType + " Request "
          + " assigned to " + provider.getProviderCode()
          + " for location " + LocationDisplayUtils.forLog(location)
          + "[ID " + LocationDisplayUtils.idForLog(location) + "]");
    }
  
    DateTime now = DateTime.now();
    DateTime processTime = getProcessTime(now, provider);
    
    SyncProfileTask data = new SyncProfileTask();
    data.setProcessType(SyncProcessType.PROFILE);
    data.setProcessTaskName(getProcessTaskName(SyncProcessType.PROFILE, provider, destination, processTime));
    data.setProvider(provider);
    data.setRequestType(requestType);
    data.setDestination(destination);
    data.setProfileAttribute(attribute);
  
    // Otherwise queue the Request for Sync
    return store.storeProfileTask(data, new SyncProfileProcessorTaskGenerator(data.getProcessTaskName(),
        processTime.getMillis() - now.getMillis()));
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