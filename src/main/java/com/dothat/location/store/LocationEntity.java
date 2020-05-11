package com.dothat.location.store;

import com.dothat.location.StateLookup;
import com.dothat.location.data.Country;
import com.dothat.location.data.Location;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

/**
 * Entity object to store Location information using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class LocationEntity {

  @Id
  private Long locationId;
  
  private String location;
  @Index
  String locationIndex;

  private String area;
  @Index
  String areaIndex;

  private String zone;
  @Index
  String zoneIndex;

  private String city;
  @Index
  String cityIndex;
  
  @Index
  private String state;

  @Index
  private Country country;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;

  private LocationEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public LocationEntity(Location data) {
    this();
    locationId = data.getLocationId();
    if (data.getLocation() != null) {
      location = data.getLocation();
      locationIndex = toIndexValue(data.getLocation());
    }
    if (data.getArea() != null) {
      area = data.getArea();
      areaIndex = toIndexValue(data.getArea());
    }
    if (data.getZone() != null) {
      zone = data.getZone();
      zoneIndex = toIndexValue(data.getZone());
    }
  
    city = data.getCity();
    cityIndex = toIndexValue(data.getCity());
    state = data.getState().getCode();
    country = data.getCountry();
  }
  
  public Location getData() {
    Location data = new Location();
    data.setLocationId(locationId);
    data.setLocation(location);
    data.setArea(area);
    data.setZone(zone);
    data.setCity(city);
    data.setCountry(country);
    data.setState(StateLookup.getInstance().getEnum(data.getCountry(), state));
    return data;
  }
  
  private String toIndexValue(String value) {
    return value.toUpperCase();
  }
}
