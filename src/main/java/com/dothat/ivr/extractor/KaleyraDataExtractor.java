package com.dothat.ivr.extractor;

import com.dothat.ivr.data.IVRCall;
import com.dothat.ivr.data.IVRDataField;
import com.dothat.ivr.data.IVRProvider;
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
    
    countryCodeMap.put(CountryCode.INDIA.getCode(), Country.INDIA);
    
    stateCodeMap.put("DELHI", IndiaState.DELHI);
    stateCodeMap.put("HR", IndiaState.HARYANA);
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
  public IVRCall extractCallData(String uri, JSONObject json) {
    IVRCall data = super.extractCallData(uri, json);
    // Set Call Timestamp
    DateTime now = DateTime.now();
    LocalDate today = LocalDate.now();
    // TODO(abhideep): Get this from the Web Hook / API data itself.
    data.setCallDate(JodaUtils.toSimpleDate(today));

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
