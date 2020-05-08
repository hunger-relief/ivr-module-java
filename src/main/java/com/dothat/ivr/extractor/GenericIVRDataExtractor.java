package com.dothat.ivr.extractor;

import com.dothat.ivr.data.IVRCall;
import com.dothat.ivr.data.IVRDataField;
import com.dothat.ivr.data.IVRProvider;
import com.dothat.location.data.Country;
import com.dothat.location.data.State;
import com.dothat.validator.PhoneNumberSanitizer;
import org.json.JSONObject;

import java.util.Map;

/**
 * Generic implementation of {@link IVRDataExtractor}.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class GenericIVRDataExtractor implements IVRDataExtractor {
  private final IVRProvider provider;
  private final Map<IVRDataField, String> fieldNameMap;
  private final Map<String, Country> countryCodeMap;
  private final Map<String, State<?>> stateCodeMap;
  
  public GenericIVRDataExtractor(IVRProvider provider, Map<IVRDataField, String> fieldNameMap,
                                 Map<String, Country> countryCodeMap, Map<String, State<?>> stateCodeMap) {
    this.provider = provider;
    this.fieldNameMap = fieldNameMap;
    this.countryCodeMap = countryCodeMap;
    this.stateCodeMap = stateCodeMap;
  }
  
  @Override
  public IVRProvider getProvider() {
    return provider;
  }
  
  @Override
  public IVRCall extractCallData(String uri, JSONObject json) {
    IVRCall data = new IVRCall();
    data.setProvider(provider);
    data.setNotificationUri(uri);

    data.setCountryValue(getValue(json, IVRDataField.COUNTRY));
    Country country = countryCodeMap.get(data.getCountryValue());
    data.setCountry(country);
    data.setStateValue(getValue(json, IVRDataField.STATE));
    State<?> state = stateCodeMap.get(data.getStateValue());
    data.setState(state == null ? null : state.getCode());
  
    data.setCallerNumber(getPhone(country, json, IVRDataField.CALLER_NUMBER));
    data.setIvrVirtualNumber(getPhone(country, json, IVRDataField.VIRTUAL_NUMBER));
    data.setIvrNumber(getPhone(country, json, IVRDataField.IVR_NUMBER));

    return data;
  }
  
  private String getValue(JSONObject json, IVRDataField field) {
    String fieldName = fieldNameMap.get(field);
    return (String) json.get(fieldName);
  }
  
  private String getPhone(Country country, JSONObject json, IVRDataField field) {
    String phone = getValue(json, field);
    return new PhoneNumberSanitizer(country).sanitize(phone);
  }
}
