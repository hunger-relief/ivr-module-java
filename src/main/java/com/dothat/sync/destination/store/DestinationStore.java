package com.dothat.sync.destination.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.location.data.Location;
import com.dothat.location.store.LocationStore;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.provider.store.ProviderStore;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.destination.data.Destination;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Store to manage Destinations using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class DestinationStore {
  private static final Logger logger = LoggerFactory.getLogger(DestinationStore.class);
  
  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(DestinationEntity.class);
    
    // Initialize Dependencies
    LocationStore.init();
    ProviderStore.init();
  }
  
  public Long store(Destination data) {
    return PersistenceService.service().transact(() -> {
      
      // Save the data
      DestinationEntity destination = new DestinationEntity(data);
      Key<DestinationEntity> key = PersistenceService.service().save().entity(destination).now();
      
      // Extract the Destination Id
      Long destinationId = key.getId();
      
      // Set the Destination Id for new Destinations
      data.setDestinationId(destinationId);
      return destinationId;
    });
  }
  
  public Destination find(ReliefProvider data, RequestType requestType, Location location) {
    Long locationId = null;
    if (location != null) {
      locationId = location.getLocationId();
    }
    
    List<DestinationEntity> destinationList = PersistenceService.service().load()
        .type(DestinationEntity.class)
        .filter("provider", data.getProviderId())
        .filter("requestType", requestType)
        .filter("location", locationId)
        .list();
    
    if (destinationList == null || destinationList.isEmpty()) {
      return null;
    }
    if (destinationList.size() == 1) {
      return destinationList.get(0).getData();
    }
    throw new IllegalStateException("More than one Destination defined for Provider "
        + data.getProviderId() + " for Request Type " + requestType + " and Location " + locationId);
  }
  
  public List<Destination> findAll(ReliefProvider data, RequestType requestType, Location location) {
    List<RequestType> requestTypes = Lists.newArrayList();
    List<Long> locationIds = Lists.newArrayList();
    requestTypes.add(null);
    locationIds.add(null);
    if (requestType != null) {
      logger.info("Adding Destinations with Request Type {} for Provider with Code {}",
          requestType, data.getProviderCode());
      requestTypes.add(requestType);
    }
    if (location != null && location.getLocationId() != null) {
      logger.info("Adding Destinations with Location Id {} for Provider with Code {}",
          location.getLocationId(), data.getProviderCode());
      locationIds.add(location.getLocationId());
    }

    List<DestinationEntity> destinationList = PersistenceService.service().load()
        .type(DestinationEntity.class)
        .filter("provider", data.getProviderId())
        .filter("requestType IN ", requestTypes)
        .filter("location IN ", locationIds)
        .list();
    
    if (destinationList == null || destinationList.isEmpty()) {
      return null;
    }
    List<Destination> dataList = new ArrayList<>();
    for (DestinationEntity destination : destinationList) {
      dataList.add(destination.getData());
    }
    return dataList;
  }
}
