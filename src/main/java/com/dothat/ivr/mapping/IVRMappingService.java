package com.dothat.ivr.mapping;

import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.ivr.mapping.store.IVRMappingStore;
import com.dothat.location.LocationService;
import com.dothat.location.data.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service Layer to manage mapping betweeb IVR Number and Location / Service Type.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRMappingService {
  private static final Logger logger = LoggerFactory.getLogger(IVRMappingService.class);
  
  private final IVRMappingStore store = new IVRMappingStore();

  public Long register(IVRMapping data) {

    if (data.getCircle() == null) {
      logger.info("Creating Mapping for phone number " + data.getPhoneNumber());
    } else {
      logger.info("Creating Mapping for phone number " + data.getPhoneNumber() + " and circle " + data.getCircle());
    }

    // Looking up the existing mapping by phone number and circle.
    IVRMapping currentMapping = lookup(data.getPhoneNumber(), data.getCircle());
    if (currentMapping != null) {
      data.setMappingId(currentMapping.getMappingId());
    }

    // Lookup the location
    if (data.getLocation() == null || data.getLocation().getLocationId() == null) {
      throw new IllegalArgumentException("Location is required to create Mapping for phone " + data.getPhoneNumber());
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
      return lookup(phoneNumber, circle, true);
    }
    
    public IVRMapping lookup(String phoneNumber, String circle, boolean exactMatch) {
      IVRMapping phoneOnlyMatch = null;
    
      IVRMapping data = new IVRMapping();
      data.setPhoneNumber(phoneNumber);
      data.setCircle(circle);
      if (circle == null) {
        logger.info("Finding IVR Mapping for phone number {}", phoneNumber);
      } else {
        logger.info("Finding IVR Mapping for phone number {} and circle {}", phoneNumber, circle);
      }
  
      List<IVRMapping> currentMappings = store.find(phoneNumber, exactMatch ? circle : null);
      if (currentMappings != null) {
        for (IVRMapping mapping : currentMappings) {
          if (mapping.isKeyIdentical(data)) {
            return mapping;
          }
          if (mapping.getCircle() == null && !exactMatch) {
            phoneOnlyMatch = mapping;
          }
        }
      }
      return phoneOnlyMatch;
    }
}
