package com.dothat.location;

import com.dothat.location.data.Location;
import com.dothat.location.store.LocationStore;

/**
 * Service that registers location or retrieves a registered location.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class LocationRegistrationService {
  private final LocationStore store = new LocationStore();

  public Long register(Location data) {
    //TODO(abhideep): Add validation
    return store.store(data);
  }
  
  public Location lookup(Location data) {
    return null;
  }
}
