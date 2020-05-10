package com.dothat.ivr.mapping.data;

import com.dothat.location.data.Location;
import com.dothat.relief.request.data.RequestType;
import com.google.api.server.spi.types.DateAndTime;

import java.util.Objects;

/**
 * Mapping between an IVR Number and the Location and Service (optional) that is number maps to.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRMapping {
  private Long mappingId;
  private String phoneNumber;
  private String circle;

  private Location location;
  private RequestType requestType;
  
  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getMappingId() {
    return mappingId;
  }
  
  public void setMappingId(Long mappingId) {
    this.mappingId = mappingId;
  }
  
  public String getPhoneNumber() {
    return phoneNumber;
  }
  
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  
  public String getCircle() {
    return circle;
  }
  
  public void setCircle(String circle) {
    this.circle = circle;
  }
  
  public Location getLocation() {
    return location;
  }
  
  public void setLocation(Location location) {
    this.location = location;
  }
  
  public RequestType getRequestType() {
    return requestType;
  }
  
  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }
  
  public DateAndTime getCreationTimestamp() {
    return creationTimestamp;
  }
  
  public void setCreationTimestamp(DateAndTime creationTimestamp) {
    this.creationTimestamp = creationTimestamp;
  }
  
  public DateAndTime getModificationTimestamp() {
    return modificationTimestamp;
  }
  
  public void setModificationTimestamp(DateAndTime modificationTimestamp) {
    this.modificationTimestamp = modificationTimestamp;
  }
  
  public boolean isKeyIdentical(IVRMapping data) {
    String lhsCircle = null;
    String rhsCircle = null;
    if (circle != null) {
      lhsCircle = circle.toUpperCase();
    }
    if (data != null && data.circle != null) {
      rhsCircle = data.circle.toUpperCase();
    }
    return data != null
        && Objects.equals(phoneNumber, data.phoneNumber)
        && Objects.equals(lhsCircle, rhsCircle);
  }
}
