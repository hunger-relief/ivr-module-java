package com.dothat.ivr.mapping.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.ivr.mapping.data.IVRNodeMapping;
import com.dothat.ivr.notif.data.IVRProvider;
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
public class IVRNodeMappingEntity {
  @Id
  private Long nodeMappingId;
  
  @Index
  private IVRProvider provider;

  @Index
  private String phoneNumber;
  
  @Index
  String nodeId;
  @Index
  String response;

  String attributeName;
  String attributeValue;
  @Load
  Ref<LocationEntity> location;
  RequestType requestType;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;
  
  private IVRNodeMappingEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public IVRNodeMappingEntity(IVRNodeMapping data) {
    this();
    nodeMappingId = data.getNodeMappingId();

    provider = data.getProvider();
    phoneNumber = data.getPhoneNumber();

    nodeId = data.getNodeId();
    response = data.getResponse();
    
    attributeName = data.getAttributeName();
    
    if (data.getAttributeValue() != null) {
      attributeValue = data.getAttributeValue();
    } else if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      location = Ref.create(Key.create(LocationEntity.class, data.getLocation().getLocationId()));
    } else if (data.getRequestType() != null) {
      requestType = data.getRequestType();
    }

    if (data.getCreationTimestamp() != null) {
      creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    }
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  
  }
  
  IVRNodeMapping getData() {
    IVRNodeMapping data = new IVRNodeMapping();

    data.setProvider(provider);
    data.setNodeMappingId(nodeMappingId);
    data.setPhoneNumber(phoneNumber);
    
    data.setNodeId(nodeId);
    data.setResponse(response);
    
    data.setAttributeName(attributeName);
    if (attributeValue != null) {
      data.setAttributeValue(attributeValue);
    } else if (location != null) {
      data.setLocation(location.get().getData());
    } else if (requestType != null) {
      data.setRequestType(requestType);
    }
    
    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
  
    return data;
  }
}
