package com.dothat.location;

import com.dothat.location.data.Location;
import com.dothat.location.store.LocationStore;
import com.google.common.base.Strings;

import java.util.List;

/**
 * Service that manages locations in the system.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class LocationService {
  private final LocationStore store = new LocationStore();

  public Long register(Location data) {
    if (data.getCountry() == null || data.getState() == null || Strings.isNullOrEmpty(data.getCity())) {
      throw new IllegalArgumentException("Country, State, City are all required fields");
    }

    if (Strings.isNullOrEmpty(data.getZone()) && Strings.isNullOrEmpty(data.getArea())
        && Strings.isNullOrEmpty(data.getLocation())) {
      throw new IllegalArgumentException("At least one of Zone, Area, or Location are required");
    }
    //TODO(abhideep): Add validation
    List<Location> locationList = search(data);
    if (locationList != null) {
      for (Location location : locationList) {
        if (location.isIdentical(data)) {
          return location.getLocationId();
        }
      }
    }
    return store.store(data);
  }
  
  public Location lookup(Long locationId) {
    return store.find(locationId);
  }
  
  public List<Location> search(Location data) {
    return store.findAll(data);
  }
}
