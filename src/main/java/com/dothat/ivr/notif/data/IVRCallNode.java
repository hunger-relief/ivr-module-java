package com.dothat.ivr.notif.data;

import com.dothat.location.data.Country;
import com.google.api.server.spi.types.DateAndTime;

import java.util.List;

public class IVRCallNode {
  private Long callNodeId;

  private IVRProvider provider;
  private String providerCallId;
  private String providerNodeId;
  
  private String callerNumber;
  private String ivrNumber;
  private String dialedNumber;

  private Country country;
  private String countryValue;
  
  private String keyPress;

  private DateAndTime timestamp;
  
  private String notificationUri;
  private String notificationContent;
  
  private ParseStatus parseStatus;
  private List<ParseError> errorList;
  
  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getCallNodeId() {
    return callNodeId;
  }
  
  public void setCallNodeId(Long callNodeId) {
    this.callNodeId = callNodeId;
  }
  
  public IVRProvider getProvider() {
    return provider;
  }
  
  public void setProvider(IVRProvider provider) {
    this.provider = provider;
  }
  
  public String getProviderCallId() {
    return providerCallId;
  }
  
  public void setProviderCallId(String providerCallId) {
    this.providerCallId = providerCallId;
  }
  
  public String getProviderNodeId() {
    return providerNodeId;
  }
  
  public void setProviderNodeId(String providerNodeId) {
    this.providerNodeId = providerNodeId;
  }
  
  public String getCallerNumber() {
    return callerNumber;
  }
  
  public void setCallerNumber(String callerNumber) {
    this.callerNumber = callerNumber;
  }
  
  public String getIvrNumber() {
    return ivrNumber;
  }
  
  public void setIvrNumber(String ivrNumber) {
    this.ivrNumber = ivrNumber;
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
  
  public String getCountryValue() {
    return countryValue;
  }
  
  public void setCountryValue(String countryValue) {
    this.countryValue = countryValue;
  }
  
  public String getKeyPress() {
    return keyPress;
  }
  
  public void setKeyPress(String keyPress) {
    this.keyPress = keyPress;
  }
  
  public DateAndTime getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(DateAndTime timestamp) {
    this.timestamp = timestamp;
  }
  
  public String getNotificationUri() {
    return notificationUri;
  }
  
  public void setNotificationUri(String notificationUri) {
    this.notificationUri = notificationUri;
  }
  
  public String getNotificationContent() {
    return notificationContent;
  }
  
  public void setNotificationContent(String notificationContent) {
    this.notificationContent = notificationContent;
  }
  
  public ParseStatus getParseStatus() {
    return parseStatus;
  }
  
  public void setParseStatus(ParseStatus parseStatus) {
    this.parseStatus = parseStatus;
  }
  
  public List<ParseError> getErrorList() {
    return errorList;
  }
  
  public void setErrorList(List<ParseError> errorList) {
    this.errorList = errorList;
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
