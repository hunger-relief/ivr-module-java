package com.dothat.ivr.store;

import com.dothat.ivr.data.IVRCall;
import com.dothat.ivr.data.IVRProvider;
import com.dothat.location.data.Country;
import com.dothat.objectify.JodaUtils;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Entity to store IVR call data using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class IVRCallEntity {
  @Id
  private Long callId;

  @Index
  private IVRProvider provider;
  private String providerCallId;
  
  @Index
  private String callerNumber;
  private String ivrNumber;
  private String virtualNumber;
  
  private Country country;
  private String state;
  private String locationHint;
  @Index
  private String countryCode;
  @Index
  private String stateCode;
  
  @Index
  private LocalDate callDate;
  private DateTime startTimestamp;
  private DateTime endTimestamp;
  
  private String notificationUri;
  private String notificationContent;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;
  
  private IVRCallEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public IVRCallEntity(IVRCall data) {
    this();
    callId = data.getCallId();

    provider = data.getProvider();
    providerCallId = data.getProviderCallId();

    callerNumber = data.getCallerNumber();
    ivrNumber = data.getIvrNumber();
    virtualNumber = data.getIvrVirtualNumber();

    country = data.getCountry();
    countryCode = data.getCountryCode();
    state = data.getState();
    stateCode = data.getStateCode();
    locationHint = data.getLocationHint();

    callDate = JodaUtils.toLocalDate(data.getCallDate(), true);
    startTimestamp = JodaUtils.toDateTime(data.getStartTimestamp());
    endTimestamp = JodaUtils.toDateTime(data.getEndTimestamp());

    notificationUri = data.getNotificationUri();
    notificationContent = data.getNotificationContent();

    if (data.getCreationTimestamp() != null) {
      creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    }
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  }
  
  IVRCall getData() {
    IVRCall data = new IVRCall();
    data.setCallId(callId);
    
    data.setProvider(provider);
    data.setProviderCallId(providerCallId);
    
    data.setCallerNumber(callerNumber);
    data.setIvrNumber(ivrNumber);
    data.setIvrVirtualNumber(virtualNumber);
    
    data.setCountry(country);
    data.setCountryCode(countryCode);
    data.setState(state);
    data.setStateCode(stateCode);
    data.setLocationHint(locationHint);

    data.setCallDate(JodaUtils.toSimpleDate(callDate));
    data.setStartTimestamp(JodaUtils.toDateAndTime(startTimestamp));
    data.setEndTimestamp(JodaUtils.toDateAndTime(endTimestamp));
    
    data.setNotificationUri(notificationUri);
    data.setNotificationContent(notificationContent);
    
    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
    return data;
  }
}
