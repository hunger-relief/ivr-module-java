package com.dothat.ivr.notif.task;

import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.ivr.mapping.IVRMappingService;
import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.*;

/**
 * Generates a Relief Request based on Call Notification.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefRequestGenerator {
  private final IdentityService identityService = new IdentityService();
  private final IVRMappingService mappingService = new IVRMappingService();
  private final ReliefProviderService providerService = new ReliefProviderService();
  
  ReliefRequest generate(IVRCall call) {
    ReliefRequest data = new ReliefRequest();

    data.setSourceType(SourceType.IVR);
    data.setSource(call.getCallerNumber());
    
    // First get the UUID
    ObfuscatedID obfId = identityService.registerNumber(call.getCallerNumber());
    data.setRequesterID(obfId);

    // Then the Location, Service based on the number that was called.
    IVRMapping mapping = mappingService.lookup(call.getDialedNumber(), call.getCircle(), false);
    Location location = mapping.getLocation();
    RequestType requestType = mapping.getRequestType();
    data.setLocation(location);
    data.setRequestType(requestType);

    // Now find a Provider for the Request
    // TODO(abhideep): See if this can be moved out to different service.
    ReliefProvider provider = providerService.assignProvider(obfId, requestType, location);
    data.setProvider(provider);

    data.setAssignmentStatus(provider == null ? AssignmentStatus.UNASSIGNED : AssignmentStatus.ASSIGNED);
    data.setRequestStatus(RequestStatus.RECEIVED);
    data.setVerificationStatus(VerificationStatus.UNVERIFIED);
    return data;
  }
}
