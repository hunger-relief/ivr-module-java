package com.dothat.ivr.mapping;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.ivr.mapping.data.IVRNodeMapping;
import com.dothat.ivr.mapping.store.IVRMappingStore;
import com.dothat.location.LocationService;
import com.dothat.location.data.Location;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

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
      logger.info("Creating Mapping for phone number " + data.getPhoneNumber()
          + " and circle " + data.getCircle());
    }

    // Looking up the existing mapping by phone number and circle.
    IVRMapping currentMapping = lookup(data.getPhoneNumber(), data.getCircle(), true);
    if (currentMapping != null) {
      data.setMappingId(currentMapping.getMappingId());
      data.setCreationTimestamp(data.getCreationTimestamp());
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
  
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    
    // Save the mapping
    return store.store(data);
  }
  
  public IVRMapping lookup(String phoneNumber, String circle, boolean exactMatch) {
    IVRMapping phoneOnlyMatch = null;
    
    if (circle == null) {
      logger.info("Finding IVR Mapping for phone number {}", phoneNumber);
    } else {
      logger.info("Finding IVR Mapping for phone number {} and circle {}", phoneNumber, circle);
    }
    
    List<IVRMapping> currentMappings = store.find(phoneNumber, exactMatch ? circle : null);
    if (currentMappings != null) {
      for (IVRMapping mapping : currentMappings) {
        if (isKeyIdentical(phoneNumber, circle, mapping.getPhoneNumber(), mapping.getCircle())) {
          return mapping;
        }
        if (mapping.getCircle() == null && !exactMatch) {
          phoneOnlyMatch = mapping;
        }
      }
    }
    return phoneOnlyMatch;
  }
  
  public Long registerNode(IVRNodeMapping data) {
    if (data.getCircle() == null) {
      logger.info("Creating Node Mapping for phone number " + data.getPhoneNumber());
    } else {
      logger.info("Creating Node Mapping for phone number " + data.getPhoneNumber()
          + " and circle " + data.getCircle());
    }
  
    // Looking up the existing mapping by phone number and circle.
    IVRNodeMapping currentMapping = lookupNode(data.getPhoneNumber(), data.getCircle(),
        data.getNodeId(), data.getResponse(), true);
    if (currentMapping != null) {
      data.setNodeMappingId(currentMapping.getNodeMappingId());
      data.setCreationTimestamp(data.getCreationTimestamp());
    }
  
    // Lookup the location if it is there
    if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      Location location = new LocationService().lookup(data.getLocation().getLocationId());
      if (location == null) {
        throw new IllegalArgumentException("No location found with Id " + data.getLocation().getLocationId());
      }
      data.setLocation(location);
    }
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    
    // Save the mapping
    return store.storeNode(data);
  }
  
  private IVRNodeMapping lookupNode(String phoneNumber, String circle,
                                    String nodeId, String response,
                                    boolean exactMatch) {
    IVRNodeMapping phoneOnlyMatch = null;
  
    if (circle == null) {
      logger.info("Finding IVR Node Mapping for phone number {} and node {} with response {}",
          phoneNumber, nodeId, response);
    } else {
      logger.info("Finding IVR Node Mapping for phone number {} and circle {} and node {} with response {}",
          phoneNumber, circle, nodeId, response);
    }
  
    List<IVRNodeMapping> currentMappings = store.findNode(phoneNumber, exactMatch ? circle : null, nodeId, response);
    if (currentMappings != null) {
      for (IVRNodeMapping mapping : currentMappings) {
        if (isKeyIdentical(phoneNumber, circle, mapping.getPhoneNumber(), mapping.getCircle())) {
          return mapping;
        }
        if (mapping.getCircle() == null && !exactMatch) {
          phoneOnlyMatch = mapping;
        }
      }
    }
    return phoneOnlyMatch;
  }
  
  private boolean isKeyIdentical(String lhsPhone, String lhsCircle, String rhsPhone, String rhsCircle) {
    if (lhsCircle != null) {
      lhsCircle = lhsCircle.toUpperCase();
    }
    if (rhsCircle != null) {
      rhsCircle = rhsCircle.toUpperCase();
    }
    return Objects.equals(lhsPhone, rhsPhone)
        && Objects.equals(lhsCircle, rhsCircle);
  }
}
