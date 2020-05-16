package com.dothat.ivr.mapping.data;

import com.dothat.location.data.Location;
import com.dothat.relief.request.data.RequestType;
import com.google.api.server.spi.types.DateAndTime;

/**
 * Maps the Response received by IVR System Node into Answers (Attribute Values).
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRNodeMapping {
  Long nodeMappingId;
  
  private String phoneNumber;
  private String circle;
  
  private String nodeId;
  private String response;
  
  private String attributeName;

  private String attributeValue;
  private Location location;
  private RequestType requestType;
  
  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getNodeMappingId() {
    return nodeMappingId;
  }
  
  public void setNodeMappingId(Long nodeMappingId) {
    this.nodeMappingId = nodeMappingId;
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
  
  public String getNodeId() {
    return nodeId;
  }
  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }
  
  public String getResponse() {
    return response;
  }
  
  public void setResponse(String response) {
    this.response = response;
  }
  
  public String getAttributeName() {
    return attributeName;
  }
  
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }
  
  public String getAttributeValue() {
    return attributeValue;
  }
  
  public void setAttributeValue(String attributeValue) {
    this.attributeValue = attributeValue;
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
}
