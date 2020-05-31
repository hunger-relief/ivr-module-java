package com.dothat.sync;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.location.LocationDisplayUtils;
import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.data.SyncProcessType;
import com.dothat.sync.data.SyncRequestTask;
import com.dothat.sync.destination.DestinationService;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.destination.data.DestinationType;
import com.dothat.sync.store.SyncRequestStore;
import com.dothat.sync.taskgen.SyncRequestProcessorTaskGenerator;
import com.google.common.base.Strings;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Service Layer to Manage the sync process.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncService {

  private final SyncRequestStore store = new SyncRequestStore();
  
  public Long createSyncTask(ReliefRequest request) {
  
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
    data.setProcessTaskName(getProcessTaskName(provider, request.getRequestType(), destination));
    data.setProvider(provider);
    data.setRequestType(request.getRequestType());
    data.setDestination(destination);
    data.setReliefRequest(request);

    // Otherwise queue the Request for Sync
    return store.storeTask(data, new SyncRequestProcessorTaskGenerator(data.getProcessTaskName()));
  }
  
  private String getProcessTaskName(ReliefProvider provider, RequestType requestType, Destination destination) {
    return provider.getProviderCode() + "|" + SyncProcessType.REQUEST.name()
        + "|" + requestType + "|" + destination.getGoogleSheetId();
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
      if (currentTimestamp.toDate().equals(requestTimestamp.toDate())
          && requestTimestamp.isBefore(currentTimestamp)) {
        return true;
      }
    }
    return false;
  }
  
  public List<SyncRequestTask> getTasks(String taskName) {
    List<SyncRequestTask> tasks = store.findTasks(taskName);
    if (tasks == null || tasks.isEmpty()) {
      return null;
    }
    return tasks;
  }
}
