package com.dothat.ivr.mapping;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.ivr.mapping.data.IVRNodeMapping;
import com.dothat.ivr.mapping.store.IVRMappingStore;
import com.dothat.ivr.notif.data.IVRProvider;
import com.dothat.location.LocationService;
import com.dothat.location.data.Location;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service Layer to manage IVR Node to Attribute Mapping.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRNodeMappingService {
  private static final Logger logger = LoggerFactory.getLogger(IVRMappingService.class);
  
  private final IVRMappingStore store = new IVRMappingStore();
  
  public Long registerNode(IVRNodeMapping data) {
    if (Strings.isNullOrEmpty(data.getPhoneNumber())) {
      logger.info("Creating Node Mapping for Node {} for Provider {} and Phone number {}",
          data.getNodeId(), data.getProvider(), data.getPhoneNumber());
    } else {
      logger.info("Creating Node Mapping for Node {} foe Provider {}",
          data.getNodeId(), data.getProvider());
    }
    
    // Looking up the existing mapping by phone number and circle.
    IVRNodeMapping currentMapping = store.findNode(data.getProvider(), data.getPhoneNumber(),
        data.getNodeId(), data.getResponse());
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
  
  public IVRNodeMapping findMatch(IVRProvider provider, String phone, String nodeId, String keyPress) {
    // First search a Phone Number and Circle specific mapping.
    IVRNodeMapping mapping = store.findNode(provider, phone, nodeId, keyPress);
    if (mapping != null) {
      return mapping;
    }

    // Find the common Mapping for the KeyPress for all numbers of the IVRProvider
    List<IVRNodeMapping> mappingList = store.findAll(provider, null, nodeId);
    IVRNodeMapping defaultMapping = null;
    if (mappingList != null) {
      for (IVRNodeMapping nodeMapping : mappingList) {
        if (Strings.isNullOrEmpty(nodeMapping.getResponse())) {
          defaultMapping = nodeMapping;
        } else if ((nodeMapping.getResponse().equals(keyPress))) {
          if (nodeMapping.getAttributeValue() == null && nodeMapping.getLocation() == null
              && nodeMapping.getRequestType() == null) {
            nodeMapping.setAttributeValue(keyPress);
          }
          return nodeMapping;
        }
      }
    }
    return defaultMapping;
  }
}
