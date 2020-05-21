package com.dothat.sync.destination;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.destination.data.DestinationType;
import com.dothat.sync.destination.store.DestinationStore;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Service Layer to manage destinations.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DestinationService {
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
    List<Destination> destinationList = store.findAll(provider, requestType, location);
    if (destinationList == null || destinationList.isEmpty()) {
      return null;
    }
    if (destinationList.size() == 1) {
      Destination destination = destinationList.get(0);
      if (destination.getDestinationType() == destinationType) {
        return destination;
      }
      return null;
    }

    Destination defaultMatch = null;
    Destination requestTypeMatch = null;
    Destination locationMatch = null;
    for (Destination destination : destinationList) {
      if (destination.getDestinationType() != destinationType) {
        continue;
      }
      if (destination.getRequestType() == null && destination.getLocation() == null) {
        defaultMatch = destination;
      } else if (destination.getRequestType() != null && destination.getLocation() != null) {
        return destination;
      } else if (destination.getRequestType() != null) {
        requestTypeMatch = destination;
      } else {
        locationMatch = destination;
      }
    }
    
    if (locationMatch != null) {
      return locationMatch;
    }
    if (requestTypeMatch != null) {
      return requestTypeMatch;
    }
    
    return defaultMatch;
  }
}
