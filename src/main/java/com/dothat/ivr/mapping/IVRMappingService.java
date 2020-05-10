package com.dothat.ivr.mapping;

import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.ivr.mapping.store.IVRMappingStore;
import com.dothat.location.LocationService;
import com.dothat.location.data.Location;

import java.util.List;

/**
 * Service Layer to manage mapping betweeb IVR Number and Location / Service Type.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRMappingService {
  IVRMappingStore store = new IVRMappingStore();

  public Long register(IVRMapping data) {
    // Looking up the existing mapping by phone number and circle.
    IVRMapping currentMapping = lookup(data.getPhoneNumber(), data.getCircle());
    if (currentMapping != null) {
      data.setMappingId(currentMapping.getMappingId());
    }

    // Lookup the location
    if (data.getLocation() == null || data.getLocation().getLocationId() == null) {
      throw new IllegalArgumentException("Location is required to create for Mapping");
    }
    Location location = new LocationService().lookup(data.getLocation().getLocationId());
    if (location == null) {
      throw new IllegalArgumentException("No location found with Id " + data.getLocation().getLocationId());
    }
    data.setLocation(location);

    // Save the mapping
    return store.store(data);
  }
  
  public IVRMapping lookup(String phoneNumber, String circle) {
    IVRMapping data = new IVRMapping();
    data.setPhoneNumber(phoneNumber);
    data.setCircle(circle);

    List<IVRMapping> currentMappings = store.find(phoneNumber, circle);
    if (currentMappings != null) {
      for (IVRMapping mapping : currentMappings) {
        if (mapping.isKeyIdentical(data)) {
          return mapping;
        }
      }
    }
    return null;
  }
}
