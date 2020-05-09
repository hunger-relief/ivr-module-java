package com.dothat.location.store;

import com.dothat.location.data.Location;
import com.dothat.objectify.JodaUtils;
import com.dothat.objectify.PersistenceService;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;

/**
 * Stores the data for a Location.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class LocationStore {
  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(LocationEntity.class);
  }
  
  public Long store(Location data) {
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    
    return PersistenceService.service().transact(() -> {
    
      // Save the data
      LocationEntity location = new LocationEntity(data);
      Key<LocationEntity> key = PersistenceService.service().save().entity(location).now();
    
      // Extract the Location Id
      Long locationId = key.getId();
    
      // Set the Call Id on the Call Data for new Calls
      data.setLocationId(locationId);
      return locationId;
    });
  }
}
