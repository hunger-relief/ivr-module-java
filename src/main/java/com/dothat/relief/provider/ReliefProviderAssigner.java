package com.dothat.relief.provider;

import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.LocationDisplayUtils;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.AssignInstruction;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestSource;
import com.dothat.relief.provider.store.ProviderStore;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Assigns a Relief Provider to a Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefProviderAssigner {
  private static final Logger logger = LoggerFactory.getLogger(ReliefProviderAssigner.class);
  
  private final ProviderStore store = new ProviderStore();
  
  public ReliefProvider assignProvider(ObfuscatedID requesterId, RequestSource requestSource,
                                       RequestType requestType, Location location) {
    RequestSource source = null;
    if (requestSource != null) {
      if (requestSource.getSourceType() == SourceType.IVR) {
        source = new RequestSource();
        source.setSourceType(requestSource.getSourceType());
        source.setCountry(requestSource.getCountry());
        source.setDialedNumber(requestSource.getDialedNumber());
      }
    }
    
    if (source != null) {
      logger.info("Finding Provider Assignment Instructions for " + source.getSourceType()
          + " " + source.getDialedNumber() + " for Request Type " + requestType + " and Location "
          + LocationDisplayUtils.forLog(location));
      // First lookup Association by Request Source, Location, and Request Type.
      List<AssignInstruction> providerList = store.findAllInstructions(source, requestType, location);

      if (providerList == null || providerList.isEmpty()) {
        logger.info("Finding Provider Assignment Instructions for " + source.getSourceType()
            + " " + source.getDialedNumber() + " irrespective of Location");
        // Then lookup Association by just the Request Source, to see if a number is reserved for a Provider.
        providerList = store.findAllInstructions(source, requestType, null);
      }

      if (providerList != null && providerList.size() == 1) {
        return providerList.get(0).getProvider();
      }
    }
    
    // Otherwise lookup all providers for Request Type and Location and assign it to one.
    // TODO(abhideep): Add Logic for better distribution of requests.
    logger.info("Finding Provider Assignment Instructions for  for Request Type " + requestType
        + " and Location " + LocationDisplayUtils.forLog(location));
    
    List<AssignInstruction> providerList = store.findAllInstructions(null, requestType, location);
    if (providerList != null && !providerList.isEmpty()) {
      // TODO(abhideep): Check if any of the Providers have processed this person before.
      // TODO(abhideep): Improve algorithm for assignment.
      int assignIndex = requesterId.hashCode() % providerList.size();
      AssignInstruction provider = providerList.get(assignIndex);
      if (provider != null && provider.getProvider() != null) {
        return provider.getProvider();
      }
    }
    return null;
  }
}
