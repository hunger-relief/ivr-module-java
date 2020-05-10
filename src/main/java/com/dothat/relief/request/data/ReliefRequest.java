package com.dothat.relief.request.data;

import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ReliefProvider;
import com.google.api.server.spi.types.DateAndTime;

/**
 * Data for a Relief Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefRequest {
  private Long requestId;

  private ObfuscatedID requesterID;
  private RequestType requestType;

  private Location location;

  private String source;
  private SourceType sourceType;
  
  private ReliefProvider provider;

  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getRequestId() {
    return requestId;
  }
  
  public void setRequestId(Long requestId) {
    this.requestId = requestId;
  }
  
  public ObfuscatedID getRequesterID() {
    return requesterID;
  }
  
  public void setRequesterID(ObfuscatedID requesterID) {
    this.requesterID = requesterID;
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
  
  public String getSource() {
    return source;
  }
  
  public void setSource(String source) {
    this.source = source;
  }
  
  public SourceType getSourceType() {
    return sourceType;
  }
  
  public void setSourceType(SourceType sourceType) {
    this.sourceType = sourceType;
  }
  
  public ReliefProvider getProvider() {
    return provider;
  }
  
  public void setProvider(ReliefProvider provider) {
    this.provider = provider;
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
