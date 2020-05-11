package com.dothat.ivr.notif.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.ivr.notif.data.IVRProvider;
import com.dothat.ivr.notif.data.ParseError;
import com.dothat.ivr.notif.data.ParseStatus;
import com.dothat.location.data.Country;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity to store IVR call node notification using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class IVRCallNodeEntity {
  @Id
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
  
  private DateTime timestamp;
  
  private String notificationUri;
  private String notificationContent;
  
  @Index
  private ParseStatus parseStatus;
  private List<ParseErrorEntity> errorList;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;
  
  private IVRCallNodeEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public IVRCallNodeEntity(IVRCallNode data) {
    this();
    callNodeId = data.getCallNodeId();
  
    provider = data.getProvider();
    providerCallId = data.getProviderCallId();
    providerNodeId = data.getProviderNodeId();
  
    callerNumber = data.getCallerNumber();
    dialedNumber = data.getDialedNumber();
    ivrNumber = data.getIvrNumber();
    
    country = data.getCountry();
    countryValue = data.getCountryValue();
    
    keyPress = data.getKeyPress();

    notificationUri = data.getNotificationUri();
    notificationContent = data.getNotificationContent();
  
    timestamp = JodaUtils.toDateTime(data.getTimestamp());
    
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
  
  IVRCallNode getData() {
    IVRCallNode data = new IVRCallNode();
    data.setCallNodeId(callNodeId);
    
    data.setProvider(provider);
    data.setProviderNodeId(providerNodeId);
    data.setProviderCallId(providerCallId);
  
    data.setCallerNumber(callerNumber);
    data.setIvrNumber(ivrNumber);
    data.setDialedNumber(dialedNumber);
  
    data.setCountry(country);
    data.setCountryValue(countryValue);

    data.setKeyPress(keyPress);

    data.setTimestamp(JodaUtils.toDateAndTime(timestamp));
    
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
