package com.dothat.ivr.extractor;

import com.dothat.ivr.data.*;
import com.dothat.location.data.Country;
import com.dothat.location.data.State;
import com.dothat.validator.PhoneNumberSanitizer;
import com.google.common.base.Strings;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generic implementation of {@link IVRDataExtractor}.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public abstract class GenericIVRDataExtractor implements IVRDataExtractor {
  private static final Logger logger = LoggerFactory.getLogger(GenericIVRDataExtractor.class);
  
  private final IVRProvider provider;
  private final List<ParseError> errorList = new ArrayList<>();
  
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
    data.setProviderCallId(getValue(json, IVRDataField.CALL_ID));

    String countryValue = getValue(json, IVRDataField.COUNTRY);
    data.setCountryValue(countryValue);
    Country country = Country.UNKNOWN;
    if (Strings.isNullOrEmpty(countryValue)) {
      country = getCountryCodeMap().get(data.getCountryValue());
      if (country == null) {
        errorList.add(new ParseError(ParseErrorType.INVALID_VALUE, IVRDataField.COUNTRY));
        country = Country.UNKNOWN;
      }
    }
    data.setCountry(country);
    
    data.setCircle(getValue(json, IVRDataField.CIRCLE));
    State<?> state = getStateCodeMap().get(data.getCircle());
    // State is optional, so let's not mark is it as a parse error
    data.setState(state == null ? "UNKNOWN" : state.getCode());
  
    data.setCallerNumber(getPhone(country, json, IVRDataField.CALLER_NUMBER));
    data.setIvrVirtualNumber(getPhone(country, json, IVRDataField.VIRTUAL_NUMBER));
    data.setIvrNumber(getPhone(country, json, IVRDataField.IVR_NUMBER));

    if (errorList.isEmpty()) {
      data.setParseStatus(ParseStatus.SUCCESS);
    } else {
      data.setParseStatus(ParseStatus.FAILED);
      data.setErrorList(errorList);
    }
    data.setNotificationContent(json.toString(2));
    return data;
  }
  
  protected String getValue(JSONObject json, IVRDataField field) {
    String fieldName = getFieldNameMap().get(field);
    if (fieldName == null) {
      errorList.add(new ParseError(ParseErrorType.MISSING_FIELD_CONFIG, field));
      logger.error("No field name defined for {}", field);
      return null;
    }
    try {
      String value = json.getString(fieldName);
      if (Strings.isNullOrEmpty(value)) {
        errorList.add(new ParseError(ParseErrorType.MISSING_VALUE, field));
        logger.error("No value retrieved for {} from {}", field, json);
      }
      return value;
    } catch (JSONException je) {
      errorList.add(new ParseError(ParseErrorType.MISSING_VALUE, field));
      logger.error("Error retrieving {} from {}", field, json);
      return null;
    }
  }
  
  protected String getPhone(Country country, JSONObject json, IVRDataField field) {
    String phone = getValue(json, field);
    if (Strings.isNullOrEmpty(phone)) {
      // Error was already added above.
      return phone;
    }
    if (country == null) {
      errorList.add(new ParseError(ParseErrorType.CANNOT_PARSE, field));
      return null;
    }
    PhoneNumberSanitizer sanitizer = new PhoneNumberSanitizer(country);
    try {
      return sanitizer.sanitize(phone);
    } catch (IllegalStateException ise) {
      errorList.add(new ParseError(ParseErrorType.CONFIG_ERROR, field));
      return phone;
    }
  }
}
