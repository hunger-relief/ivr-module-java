package com.dothat.location.data;

import com.google.api.server.spi.types.DateAndTime;

/**
 * Data object for a location or area where the service is provided.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class Location {
  private Long locationId;
  private String location;
  private String area;
  private String zone;
  private String city;
  private State<?> state;
  private Country country;
  
  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getLocationId() {
    return locationId;
  }
  
  public void setLocationId(Long locationId) {
    this.locationId = locationId;
  }
  
  public String getLocation() {
    return location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }

  public String getArea() {
    return area;
  }
  
  public void setArea(String area) {
    this.area = area;
  }
  
  public String getZone() {
    return zone;
  }
  
  public void setZone(String zone) {
    this.zone = zone;
  }
  
  public String getCity() {
    return city;
  }
  
  public void setCity(String city) {
    this.city = city;
  }
  
  public State<?> getState() {
    return state;
  }
  
  public void setState(State<?> state) {
    this.state = state;
  }
  
  public Country getCountry() {
    return country;
  }
  
  public void setCountry(Country country) {
    this.country = country;
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
