package com.dothat.relief.request.data;

import com.dothat.location.data.Country;

/**
 * Source for a Relief Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestSource {
  private SourceType sourceType;
  private String source;
  private String sourceId;

  // Details like Dialed Number and country of Dialed Number
  private String dialedNumber;
  private Country country;
  
  public SourceType getSourceType() {
    return sourceType;
  }
  
  public void setSourceType(SourceType sourceType) {
    this.sourceType = sourceType;
  }
  
  public String getSource() {
    return source;
  }
  
  public void setSource(String source) {
    this.source = source;
  }
  
  public String getSourceId() {
    return sourceId;
  }
  
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }
  
  public String getDialedNumber() {
    return dialedNumber;
  }
  
  public void setDialedNumber(String dialedNumber) {
    this.dialedNumber = dialedNumber;
  }
  
  public Country getCountry() {
    return country;
  }
  
  public void setCountry(Country country) {
    this.country = country;
  }
}
