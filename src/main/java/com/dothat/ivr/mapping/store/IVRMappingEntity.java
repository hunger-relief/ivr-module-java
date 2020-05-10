package com.dothat.ivr.mapping.store;

import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.location.store.LocationEntity;
import com.dothat.relief.request.data.RequestType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

/**
 * Entity Object to store IVR to Location / Service Mapping using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class IVRMappingEntity {
  @Id
  private Long mappingId;
  
  @Index
  private String phoneNumber;
  private String circle;
  @Index
  private String circleIndex;

  @Load @Index
  Ref<LocationEntity> location;

  @Index
  RequestType requestType;
  
  private IVRMappingEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public IVRMappingEntity(IVRMapping data) {
    mappingId = data.getMappingId();
    phoneNumber = data.getPhoneNumber();
    circle = data.getCircle();
    if (data.getCircle() != null) {
      circleIndex = data.getCircle().toUpperCase();
    }
    if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      location = Ref.create(Key.create(LocationEntity.class, data.getLocation().getLocationId()));
    }
    requestType = data.getRequestType();
  }
  
  IVRMapping getData() {
    IVRMapping data = new IVRMapping();
    data.setMappingId(mappingId);
    data.setPhoneNumber(phoneNumber);
    data.setCircle(circle);
    data.setLocation(location.get().getData());
    data.setRequestType(requestType);
    return data;
  }
}
