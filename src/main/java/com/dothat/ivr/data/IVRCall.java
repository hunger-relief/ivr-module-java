package com.dothat.ivr.data;

import com.dothat.location.data.Country;
import com.google.api.server.spi.types.DateAndTime;
import com.google.api.server.spi.types.SimpleDate;

public class IVRCall {
  private Long callId;
  private IVRProvider provider;
  private String providerCallId;
  
  private String callerNumber;
  private String ivrNumber;
  private String ivrVirtualNumber;
  
  private Country country;
  private String countryValue;
  private String state;
  private String stateValue;
  private String locationHint;
  
  private SimpleDate callDate;
  private DateAndTime startTimestamp;
  private DateAndTime endTimestamp;
  
  private String notificationUri;
  private String notificationContent;
  
  private DateAndTime creationTimestamp;
  private DateAndTime modificationTimestamp;
  
  public Long getCallId() {
    return callId;
  }
  
  public void setCallId(Long callId) {
    this.callId = callId;
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
  
  public String getIvrVirtualNumber() {
    return ivrVirtualNumber;
  }
  
  public void setIvrVirtualNumber(String ivrVirtualNumber) {
    this.ivrVirtualNumber = ivrVirtualNumber;
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
  
  public String getState() {
    return state;
  }
  
  public void setState(String state) {
    this.state = state;
  }
  
  public String getStateValue() {
    return stateValue;
  }
  
  public void setStateValue(String stateValue) {
    this.stateValue = stateValue;
  }
  
  public String getLocationHint() {
    return locationHint;
  }
  
  public void setLocationHint(String locationHint) {
    this.locationHint = locationHint;
  }
  
  public SimpleDate getCallDate() {
    return callDate;
  }
  
  public void setCallDate(SimpleDate callDate) {
    this.callDate = callDate;
  }
  
  public DateAndTime getStartTimestamp() {
    return startTimestamp;
  }
  
  public void setStartTimestamp(DateAndTime startTimestamp) {
    this.startTimestamp = startTimestamp;
  }
  
  public DateAndTime getEndTimestamp() {
    return endTimestamp;
  }
  
  public void setEndTimestamp(DateAndTime endTimestamp) {
    this.endTimestamp = endTimestamp;
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
