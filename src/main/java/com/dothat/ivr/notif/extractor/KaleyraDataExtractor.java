package com.dothat.ivr.notif.extractor;

import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.ivr.notif.data.IVRDataField;
import com.dothat.ivr.notif.data.IVRProvider;
import com.dothat.location.data.Country;
import com.dothat.location.data.State;
import com.dothat.location.impl.IndiaState;
import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.validate.phone.CountryCode;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Extracts data for a Call on Kaleyra.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class KaleyraDataExtractor extends GenericIVRDataExtractor {
  
  private final Map<IVRDataField, String> fieldNameMap = new HashMap<>();
  private final Map<String, Country> countryCodeMap = new HashMap<>();
  private final Map<String, State<?>> stateCodeMap = new HashMap<>();
  
  public KaleyraDataExtractor() {
    super(IVRProvider.KALEYRA);
    fieldNameMap.put(IVRDataField.CALLER_NUMBER, "caller");
    fieldNameMap.put(IVRDataField.DIALED_NUMBER, "called");
    fieldNameMap.put(IVRDataField.IVR_NUMBER, "number");
    fieldNameMap.put(IVRDataField.CALL_ID, "id");
    fieldNameMap.put(IVRDataField.COUNTRY, "isocode");
    fieldNameMap.put(IVRDataField.CIRCLE, "circle");
    fieldNameMap.put(IVRDataField.CALL_TIMESTAMP, "calltime");
    fieldNameMap.put(IVRDataField.CALL_DURATION, "duration");
    fieldNameMap.put(IVRDataField.CALL_NODE_ID, "nodeid");
    fieldNameMap.put(IVRDataField.KEY_PRESS, "keypress");
    
    countryCodeMap.put(CountryCode.INDIA.getCode(), Country.INDIA);
    
    stateCodeMap.put("DELHI", IndiaState.DELHI);
    stateCodeMap.put("HARYANA", IndiaState.HARYANA);
    stateCodeMap.put("TAMIL NADU", IndiaState.TAMIL_NADU);
  }
  
  @Override
  protected Map<IVRDataField, String> getFieldNameMap() {
    return fieldNameMap;
  }
  
  @Override
  protected Map<String, Country> getCountryCodeMap() {
    return countryCodeMap;
  }
  
  @Override
  protected Map<String, State<?>> getStateCodeMap() {
    return stateCodeMap;
  }
  
  @Override
  public IVRCall extractCall(String uri, JSONObject json) {
    IVRCall data = super.extractCall(uri, json);
    // Set Call Timestamp
    DateTime now = DateTime.now();
    LocalDate today = LocalDate.now();
    // TODO(abhideep): Get this from the Web Hook / API data itself.
    data.setCallDate(JodaUtils.toSimpleDate(today));

    data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    return data;
  }
  
  @Override
  public IVRCallNode extractCallNode(String uri, JSONObject json) {
    IVRCallNode data = super.extractCallNode(uri, json);

    // Set Call Timestamp
    DateTime now = DateTime.now();
    // TODO(abhideep): Get this from the Web Hook / API data itself.
    data.setTimestamp(JodaUtils.toDateAndTime(now));
  
    data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));

    return data;
  }
  
  public static class Factory implements IVRDataExtractor.Factory {
  
    @Override
    public IVRDataExtractor create() {
      return new KaleyraDataExtractor();
    }
  }
}
