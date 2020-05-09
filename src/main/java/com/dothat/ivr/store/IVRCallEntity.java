package com.dothat.ivr.store;

import com.dothat.ivr.data.IVRCall;
import com.dothat.ivr.data.IVRProvider;
import com.dothat.ivr.data.ParseError;
import com.dothat.ivr.data.ParseStatus;
import com.dothat.location.data.Country;
import com.dothat.objectify.JodaUtils;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

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
  @Index
  private String dialedNumber;
  private String ivrNumber;
  
  @Index
  private Country country;
  @Index
  private String state;
  private String locationHint;
  private String countryValue;
  private String circle;
  
  @Index
  private LocalDate callDate;
  private DateTime startTimestamp;
  private DateTime endTimestamp;
  
  private String notificationUri;
  private String notificationContent;
  
  @Index
  private ParseStatus parseStatus;
  private List<ParseErrorEntity> errorList;
  
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
    dialedNumber = data.getDialedNumber();
    ivrNumber = data.getIvrNumber();

    country = data.getCountry();
    countryValue = data.getCountryValue();
    state = data.getState();
    circle = data.getCircle();
    locationHint = data.getLocationHint();

    callDate = JodaUtils.toLocalDate(data.getCallDate(), true);
    startTimestamp = JodaUtils.toDateTime(data.getStartTimestamp());
    endTimestamp = JodaUtils.toDateTime(data.getEndTimestamp());

    notificationUri = data.getNotificationUri();
    notificationContent = data.getNotificationContent();

    parseStatus = data.getParseStatus();
    if (data.getErrorList() != null && !data.getErrorList().isEmpty()) {
      errorList = new ArrayList<>();
      for (ParseError error : data.getErrorList()) {
        errorList.add(new ParseErrorEntity(error));
      }
    }
    
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
    data.setDialedNumber(dialedNumber);
    
    data.setCountry(country);
    data.setCountryValue(countryValue);
    data.setState(state);
    data.setCircle(circle);
    data.setLocationHint(locationHint);

    data.setCallDate(JodaUtils.toSimpleDate(callDate));
    data.setStartTimestamp(JodaUtils.toDateAndTime(startTimestamp));
    data.setEndTimestamp(JodaUtils.toDateAndTime(endTimestamp));
    
    data.setNotificationUri(notificationUri);
    data.setNotificationContent(notificationContent);
    
    data.setParseStatus(parseStatus);
    if (errorList != null && !errorList.isEmpty()) {
      List<ParseError> errors = new ArrayList<>();
      for (ParseErrorEntity error : errorList) {
        errors.add(error.getData());
      }
      data.setErrorList(errors);
    }
    
    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
    return data;
  }
}
