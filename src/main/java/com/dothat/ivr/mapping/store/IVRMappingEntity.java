package com.dothat.ivr.mapping.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.ivr.mapping.data.IVRMapping;
import com.dothat.location.store.LocationEntity;
import com.dothat.relief.request.data.RequestType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import org.joda.time.DateTime;

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
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;
  
  private IVRMappingEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public IVRMappingEntity(IVRMapping data) {
    mappingId = data.getMappingId();
    phoneNumber = data.getPhoneNumber();

    if (data.getCircle() != null) {
      circle = data.getCircle();
      circleIndex = data.getCircle().toUpperCase();
    }

    if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      location = Ref.create(Key.create(LocationEntity.class, data.getLocation().getLocationId()));
    }
    if (data.getRequestType() != null) {
      requestType = data.getRequestType();
    }

    if (data.getCreationTimestamp() != null) {
      creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    }
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  
  }
  
  IVRMapping getData() {
    IVRMapping data = new IVRMapping();

    data.setMappingId(mappingId);
    data.setPhoneNumber(phoneNumber);
    data.setCircle(circle);
    data.setLocation(location.get().getData());
    data.setRequestType(requestType);
    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
  
    return data;
  }
}
