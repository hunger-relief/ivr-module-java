package com.dothat.sync.destination.data;

import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestType;
import com.google.api.server.spi.types.DateAndTime;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class Destination {
  private Long destinationId;
  
  private ReliefProvider provider;
  private RequestType requestType;
  private Location location;
  
  private DestinationType destinationType;
  // TODO(abhideep): SpreadSheet Id is enough for Google sheets
  //  but we may need more details for other types of destinations.
  private String googleSheetId;
  private String googleSheetName;
  private Integer syncFrequencyInSeconds;

  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getDestinationId() {
    return destinationId;
  }
  
  public void setDestinationId(Long destinationId) {
    this.destinationId = destinationId;
  }
  
  public RequestType getRequestType() {
    return requestType;
  }
  
  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }
  
  public ReliefProvider getProvider() {
    return provider;
  }
  
  public void setProvider(ReliefProvider provider) {
    this.provider = provider;
  }
  
  public Location getLocation() {
    return location;
  }
  
  public void setLocation(Location location) {
    this.location = location;
  }
  
  public DestinationType getDestinationType() {
    return destinationType;
  }
  
  public void setDestinationType(DestinationType destinationType) {
    this.destinationType = destinationType;
  }
  
  public String getGoogleSheetId() {
    return googleSheetId;
  }
  
  public void setGoogleSheetId(String googleSheetId) {
    this.googleSheetId = googleSheetId;
  }

  public String getGoogleSheetName() {
    return googleSheetName;
  }

  public void setGoogleSheetName(String googleSheetName) {
    this.googleSheetName = googleSheetName;
  }

  public Integer getSyncFrequencyInSeconds() {
    return syncFrequencyInSeconds;
  }

  public void setSyncFrequencyInSeconds(Integer syncFrequencyInSeconds) {
    this.syncFrequencyInSeconds = syncFrequencyInSeconds;
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
