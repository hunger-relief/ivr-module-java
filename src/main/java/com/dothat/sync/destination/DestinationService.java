package com.dothat.sync.destination;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.location.LocationDisplayUtils;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.destination.data.DestinationType;
import com.dothat.sync.destination.store.DestinationStore;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service Layer to manage destinations.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DestinationService {
  private static final Logger logger = LoggerFactory.getLogger(DestinationService.class);
  
  private final DestinationStore store = new DestinationStore();
  
  public Long register(Destination data) {
    new DestinationValidator().validate(data);
    Destination destination = store.find(data.getProvider(), data.getRequestType(), data.getLocation());
    if (destination != null) {
      data.setDestinationId(destination.getDestinationId());
      data.setCreationTimestamp(destination.getCreationTimestamp());
    }
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    
    return store.store(data);
  }
  
  public Destination lookupDestination(ReliefProvider provider, RequestType requestType,
                                       Location location, DestinationType destinationType) {
    logger.info("Looking for Destinations for " + provider.getProviderCode()
        + " [ID " + provider.getProviderId() + " ]" + " for " + requestType
        + " in " + LocationDisplayUtils.forLog(location)
        + " [ID " + LocationDisplayUtils.idForLog(location) + " ]");
    if (location != null) {
      // First, Lookup Location specific Destinations irrespective of Request Type
      List<Destination> destinationList = store.findAll(provider, null, location,
          true, true);
      
      Destination matchingDestination = null;
      if (destinationList != null) {
        logger.info("Found " + destinationList.size() + " Location specific Destinations for "
            + provider.getProviderCode() + " [ID " + provider.getProviderId() + " ]"
            + " for " + requestType
            + " in " + LocationDisplayUtils.forLog(location)
            + " [ID " + LocationDisplayUtils.idForLog(location) + " ]");
        for (Destination destination : destinationList) {
          if (destination.getDestinationType() == destinationType) {
            if (destination.getRequestType() == null) {
              matchingDestination = destination;
            } else if (destination.getRequestType() == requestType) {
              return destination;
            }
          }
        }
      }
      if (matchingDestination != null) {
        return matchingDestination;
      }
    }
  
    // Lookup all Destinations that don't have a location, but may or may not have a request Type
    List<Destination> destinationList = store.findAll(provider, null, null,
        true, false);
    Destination matchingDestination = null;
    if (destinationList != null) {
      logger.info("Found " + destinationList.size() + " Non Location Destinations for "
          + provider.getProviderCode() + " [ID " + provider.getProviderId() + " ]"
          + " for " + requestType
          + " in " + LocationDisplayUtils.forLog(location)
          + " [ID " + LocationDisplayUtils.idForLog(location) + " ]");
      for (Destination destination : destinationList) {
        if (destination.getDestinationType() == destinationType) {
          if (destination.getRequestType() == null) {
            matchingDestination = destination;
          } else if (destination.getRequestType() == requestType) {
            return destination;
          }
        }
      }
    }
    return matchingDestination;
  }
}
