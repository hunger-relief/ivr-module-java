package com.dothat.ivr.notif.task;

import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.ivr.mapping.IVRMappingService;
import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.location.LocationDisplayUtils;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.ReliefProviderAssigner;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates a Relief Request based on Call Notification.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefRequestGenerator {
  private static final Logger logger = LoggerFactory.getLogger(ReliefRequestGenerator.class);
  
  private final IdentityService identityService = new IdentityService();
  private final IVRMappingService mappingService = new IVRMappingService();
  
  ReliefRequest generate(IVRCall call) {
    ReliefRequest data = new ReliefRequest();

    data.setSourceType(SourceType.IVR);
    data.setSource(call.getProvider().name());
    data.setSourceId(call.getProviderCallId());
    data.setRequestTimestamp(call.getCallTimestamp());

    // First get the UUID
    ObfuscatedID obfId = identityService.registerNumber(call.getCallerNumber());
    data.setRequesterID(obfId);

    // Then the Location, Service based on the number that was called.
    IVRMapping mapping = mappingService.lookup(call.getDialedNumber(), call.getCircle(), false);
    logger.info("Finding Location and Request Type for Dialed Number " + call.getDialedNumber()
        + " and Circle " + call.getCircle());
    Location location  = null;
    RequestType requestType = null;
    String locationString = "";
    if (mapping != null) {
      location = mapping.getLocation();
      requestType = mapping.getRequestType();
      data.setLocation(location);
      data.setRequestType(requestType);
      locationString = LocationDisplayUtils.forError(location);
      logger.info("Call on Dialed Number " + call.getDialedNumber() + " and Circle " + call.getCircle()
          + " mapped to Request for " + requestType + " from " + locationString);
    } else {
      logger.warn("No IVR mapping found for Dialed Number " + call.getDialedNumber()
          + " and Circle " + call.getCircle());
      data.setRequestType(RequestType.UNKNOWN);
      // TODO(abhideep): Figure out what unknown location this should be assigned to.
    }

    // Now find a Provider for the Request
    RequestSource source = new RequestSource();
    source.setSourceType(data.getSourceType());
    source.setSource(data.getSource());
    source.setSourceId(data.getSourceId());
    source.setDialedNumber(call.getDialedNumber());
    source.setCountry(call.getCountry());
    ReliefProvider provider = new ReliefProviderAssigner().assignProvider(obfId, source, requestType, location);
    if (provider != null) {
      logger.info("Request for " + requestType + " from " + locationString + " assigned to "
          + provider.getProviderCode());
    } else {
      logger.warn(" No provider found for Request for " + requestType + " from " + locationString);
    }
    data.setProvider(provider);

    data.setClaimStatus(provider == null ? ClaimStatus.UNCLAIMED : ClaimStatus.CLAIMED);
    data.setRequestStatus(RequestStatus.RECEIVED);
    data.setVerificationStatus(VerificationStatus.UNVERIFIED);
    return data;
  }
}
