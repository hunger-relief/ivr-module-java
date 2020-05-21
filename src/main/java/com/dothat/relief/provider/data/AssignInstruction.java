package com.dothat.relief.provider.data;

import com.dothat.location.data.Location;
import com.dothat.relief.request.data.RequestSource;
import com.dothat.relief.request.data.RequestType;
import com.google.api.server.spi.types.DateAndTime;

/**
 * Data / Instructions for how a Request is assigned to aProvider based on Location, IVR Number
 * and the Request Source.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class AssignInstruction {
  private Long instructionId;

  private ReliefProvider provider;

  private RequestSource source;
  private RequestType requestType;
  private Location location;
  
  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getInstructionId() {
    return instructionId;
  }
  
  public void setInstructionId(Long instructionId) {
    this.instructionId = instructionId;
  }
  
  public ReliefProvider getProvider() {
    return provider;
  }
  
  public void setProvider(ReliefProvider provider) {
    this.provider = provider;
  }
  
  public RequestSource getSource() {
    return source;
  }
  
  public void setSource(RequestSource source) {
    this.source = source;
  }
  
  public RequestType getRequestType() {
    return requestType;
  }
  
  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }
  
  public Location getLocation() {
    return location;
  }
  
  public void setLocation(Location location) {
    this.location = location;
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
