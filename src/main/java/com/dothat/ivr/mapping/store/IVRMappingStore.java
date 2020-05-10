package com.dothat.ivr.mapping.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.objectify.PersistenceService;
import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.location.store.LocationStore;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Stores IVR to Location / Service Mapping using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRMappingStore {
  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(IVRMappingEntity.class);
  
    // Initialize Dependencies
    LocationStore.init();
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
  }

  public Long store(IVRMapping data) {
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
  
    return PersistenceService.service().transact(() -> {
      // Save the data
      IVRMappingEntity mapping = new IVRMappingEntity(data);
      Key<IVRMappingEntity> key = PersistenceService.service().save().entity(mapping).now();
    
      // Extract the Mapping Id
      Long mappingId = key.getId();
    
      // Set the Mapping Id on the Mapping Data for new Mappings
      data.setMappingId(mappingId);
      return mappingId;
    });
  }
  
  public List<IVRMapping> find(String phoneNumber, String circle) {
    Query<IVRMappingEntity> query = PersistenceService.service().load().type(IVRMappingEntity.class)
        .filter("phoneNumber", phoneNumber);
  
    if (!Strings.isNullOrEmpty(circle)) {
      query = query.filter("circleIndex", circle);
    }
  
    List<IVRMappingEntity> mappings = query.list();
  
    if (mappings != null) {
      List<IVRMapping> mappingList = Lists.newArrayList();
      for (IVRMappingEntity mapping : mappings) {
        mappingList.add(mapping.getData());
      }
      return mappingList;
    }
    return null;
  }
}
