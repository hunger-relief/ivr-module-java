package com.dothat.relief.request.data;

/**
 * Source for a Relief Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestSource {
  private SourceType sourceType;
  private String source;
  private String sourceId;

  // Details like Dialed Number and Spreadsheet Id
  private String dialedNumber;
  
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
}
