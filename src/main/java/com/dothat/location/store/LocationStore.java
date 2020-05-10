package com.dothat.location.store;

import com.dothat.location.data.Location;
import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.objectify.PersistenceService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import org.joda.time.DateTime;

import java.util.List;

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
  
  public static void init() {
    // Empty initialization used to trigger the static block
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
    
      // Set the Location Id on the Location Data for new Locations
      data.setLocationId(locationId);
      return locationId;
    });
  }
  
  public Location find(Long locationId) {
    LocationEntity location = PersistenceService.service().load().type(LocationEntity.class)
        .id(locationId).now();
  
    if (location == null) {
      return null;
    }
    return location.getData();
  }
  
  public List<Location> findAll(Location data) {
    Query<LocationEntity> query = PersistenceService.service().load().type(LocationEntity.class)
        .filter("country", data.getCountry())
        .filter("state", data.getState().getCode())
        .filter("cityIndex", data.getCity().toUpperCase());
    
    if (!Strings.isNullOrEmpty(data.getZone())) {
      query = query.filter("zoneIndex", data.getZone().toUpperCase());
    }
  
    if (!Strings.isNullOrEmpty(data.getArea())) {
      query = query.filter("areaIndex", data.getArea().toUpperCase());
    }
  
    if (!Strings.isNullOrEmpty(data.getLocation())) {
      query = query.filter("locationIndex", data.getLocation().toUpperCase());
    }
    List<LocationEntity> locations = query.list();
    
    if (locations != null) {
      List<Location> locationList = Lists.newArrayList();
      for (LocationEntity location : locations) {
        locationList.add(location.getData());
      }
      return locationList;
    }
    return null;
  }
}
