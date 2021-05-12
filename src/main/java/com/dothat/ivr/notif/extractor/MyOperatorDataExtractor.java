package com.dothat.ivr.notif.extractor;

import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.validate.phone.CountryCode;
import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.ivr.notif.data.IVRDataField;
import com.dothat.ivr.notif.data.IVRProvider;
import com.dothat.location.data.Country;
import com.dothat.location.impl.IndiaState;
import com.google.common.base.Strings;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts data for a Call on My Operator
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class MyOperatorDataExtractor extends GenericIVRDataExtractor {
  private static final Logger logger = LoggerFactory.getLogger(GenericIVRDataExtractor.class);

  private static final String INCOMING_CALL_TYPE_VALUE = "1";
  private static final String INCOMING_CALL_TYPE = "incoming";

  private static final String CALL_RECEIVED_STATE = "1";

  private static final String CALL_MISSED_STATUS = "2";
  private static final String CALL_VOICE_STATUS = "3";


  public MyOperatorDataExtractor() {
    super(IVRProvider.MY_OPERATOR);
    registerField(IVRDataField.CALLER_NUMBER, "caller");
    registerField(IVRDataField.DIALED_NUMBER, "called");
    registerField(IVRDataField.DIALED_NUMBER, "alt_called");
    registerField(IVRDataField.IVR_NUMBER, "rdnis");
    registerField(IVRDataField.RECEIVER_NUMBER, "users");
    registerField(IVRDataField.RELAY_MODE, "relay");
    registerField(IVRDataField.CALL_ID, "uid");
    registerField(IVRDataField.COUNTRY, "iso_code");
    registerField(IVRDataField.CALL_TIMESTAMP, "created");
    registerField(IVRDataField.CALL_TYPE, "call_type");
    registerField(IVRDataField.CALL_STATUS, "call_status");
    registerField(IVRDataField.CALL_STATE, "call_state");

    registerCountryCode(CountryCode.INDIA.getCode(), Country.INDIA);

    registerStateCode("DELHI", IndiaState.DELHI);
    registerStateCode("HARYANA", IndiaState.HARYANA);
    registerStateCode("TAMIL NADU", IndiaState.TAMIL_NADU);
  }

  @Override
  public IVRProvider getProvider() {
    return IVRProvider.MY_OPERATOR;
  }
  
  @Override
  public IVRCall extractCall(String uri, JSONObject json) {
    FieldValueExtractor extractor = new FieldValueExtractor(getFieldErrorList());
    extractor.init(getFieldNameMap());
    IVRCall data = super.extractCall(uri, json, extractor);
    String callType = extractor.extract(json, IVRDataField.CALL_TYPE);

    // We only process incoming calls for now
    if (!(INCOMING_CALL_TYPE.equals(callType)) && !(INCOMING_CALL_TYPE_VALUE.equals(callType))) {
      return null;
    }
    String callState = extractor.extract(json, IVRDataField.CALL_STATE);
    String callStatus = extractor.extract(json, IVRDataField.CALL_STATUS);
    if (CALL_MISSED_STATUS.equals(callStatus) || CALL_VOICE_STATUS.equals(callStatus)) {
      String called = extractor.extractPhone(data.getCountry(), json, IVRDataField.OVERRIDE_DIALED_NUMBER,
              false);
      if (!Strings.isNullOrEmpty(called)) {
        data.setDialedNumber(called);
      }
      logger.warn("Missing call on MyOperator from {} to {}", data.getCallerNumber(), data.getDialedNumber());
      return data;
    } else if (CALL_RECEIVED_STATE.equals(callState)) {
      data.setDialedNumber(data.getIvrNumber());
      logger.warn("Incoming call on MyOperator from {} to {}", data.getCallerNumber(), data.getDialedNumber());
      return data;
    }
    return null;
  }
  
  @Override
  public IVRCallNode extractCallNode(String uri, JSONObject json) {
    return null;
  }
  
  static class Factory implements IVRDataExtractor.Factory {
    @Override
    public IVRDataExtractor create() {
      return new MyOperatorDataExtractor();
    }
  }
}
