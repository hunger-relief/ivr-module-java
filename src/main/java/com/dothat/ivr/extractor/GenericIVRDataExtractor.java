package com.dothat.ivr.extractor;

import com.dothat.ivr.data.IVRCall;
import com.dothat.ivr.data.IVRDataField;
import com.dothat.ivr.data.IVRProvider;
import com.dothat.location.data.Country;
import com.dothat.location.data.State;
import com.dothat.validator.PhoneNumberSanitizer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Generic implementation of {@link IVRDataExtractor}.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public abstract class GenericIVRDataExtractor implements IVRDataExtractor {
  private static final Logger logger = LoggerFactory.getLogger(GenericIVRDataExtractor.class);
  
  private final IVRProvider provider;
  
  public GenericIVRDataExtractor(IVRProvider provider) {
    this.provider = provider;
  }
  
  @Override
  public IVRProvider getProvider() {
    return provider;
  }
  
  protected abstract Map<IVRDataField, String> getFieldNameMap();
  
  protected abstract Map<String, Country> getCountryCodeMap() ;
  
  protected abstract Map<String, State<?>> getStateCodeMap();
  
  @Override
  public IVRCall extractCallData(String uri, JSONObject json) {
    IVRCall data = new IVRCall();
    data.setProvider(provider);
    data.setNotificationUri(uri);

    data.setCountryValue(getValue(json, IVRDataField.COUNTRY));
    Country country = getCountryCodeMap().get(data.getCountryValue());
    data.setCountry(country);
    
    data.setStateValue(getValue(json, IVRDataField.STATE));
    State<?> state = getStateCodeMap().get(data.getStateValue());
    data.setState(state == null ? null : state.getCode());
  
    data.setCallerNumber(getPhone(country, json, IVRDataField.CALLER_NUMBER));
    data.setIvrVirtualNumber(getPhone(country, json, IVRDataField.VIRTUAL_NUMBER));
    data.setIvrNumber(getPhone(country, json, IVRDataField.IVR_NUMBER));

    data.setNotificationContent(json.toString(2));
    return data;
  }
  
  protected String getValue(JSONObject json, IVRDataField field) {
    try {
      String fieldName = getFieldNameMap().get(field);
      return json.getString(fieldName);
    } catch (Throwable t) {
      String message = "Error retrieving " + field + " from " + json;
      logger.error("Error retrieving {} from {}", field, json);
      throw new RuntimeException(message);
    }
  }
  
  protected String getPhone(Country country, JSONObject json, IVRDataField field) {
    String phone = getValue(json, field);
    return new PhoneNumberSanitizer(country).sanitize(phone);
  }
}
