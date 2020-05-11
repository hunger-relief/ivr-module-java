package com.dothat.ivr.notif.extractor;

import com.dothat.common.field.Field;
import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.field.error.FieldError;
import com.dothat.common.field.error.FieldErrorType;
import com.dothat.ivr.notif.data.*;
import com.dothat.location.data.Country;
import com.dothat.location.data.State;
import com.google.common.base.Strings;
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
  private final List<FieldError> errorList = new ArrayList<>();
  private final List<FieldError> fieldErrorList = new ArrayList<>();
  
  public GenericIVRDataExtractor(IVRProvider provider) {
    this.provider = provider;
  }
  
  @Override
  public IVRProvider getProvider() {
    return provider;
  }
  
  protected abstract Map<Field, String> getFieldNameMap();
  
  protected abstract Map<String, Country> getCountryCodeMap() ;
  
  protected abstract Map<String, State<?>> getStateCodeMap();
  
  @Override
  public IVRCall extractCall(String uri, JSONObject json) {
    IVRCall data = new IVRCall();
    FieldValueExtractor extractor = new FieldValueExtractor(fieldErrorList);
    extractor.init(getFieldNameMap());

    data.setProvider(provider);
    data.setNotificationUri(uri);
    data.setProviderCallId(extractor.extract(json, IVRDataField.CALL_ID));

    String countryValue = extractor.extract(json, IVRDataField.COUNTRY);
    data.setCountryValue(countryValue);
    Country country = Country.UNKNOWN;
    if (!Strings.isNullOrEmpty(countryValue)) {
      country = getCountryCodeMap().get(data.getCountryValue());
      if (country == null) {
        errorList.add(new FieldError(FieldErrorType.INVALID_VALUE, IVRDataField.COUNTRY));
        country = Country.UNKNOWN;
      }
    }
    data.setCountry(country);
    
    data.setCircle(extractor.extract(json, IVRDataField.CIRCLE));
    State<?> state = getStateCodeMap().get(data.getCircle());
    // State is optional, so let's not mark is it as a parse error
    data.setState(state == null ? "UNKNOWN" : state.getCode());
  
    data.setCallerNumber(extractor.extractPhone(country, json, IVRDataField.CALLER_NUMBER, true));
    data.setDialedNumber(extractor.extractPhone(country, json, IVRDataField.DIALED_NUMBER, true));
    data.setIvrNumber(extractor.extractPhone(country, json, IVRDataField.IVR_NUMBER, false));

    if (errorList.isEmpty()) {
      data.setParseStatus(ParseStatus.SUCCESS);
    } else {
      data.setParseStatus(ParseStatus.FAILED);
      data.setErrorList(toParseErrorList(errorList));
    }
    data.setNotificationContent(json.toString(2));
    return data;
  }
  
  @Override
  public IVRCallNode extractCallNode(String uri, JSONObject json) {
    IVRCallNode data = new IVRCallNode();
    data.setProvider(provider);
    FieldValueExtractor extractor = new FieldValueExtractor(errorList);
    extractor.init(getFieldNameMap());
    data.setProviderNodeId(extractor.extract(json, IVRDataField.CALL_NODE_ID));
    data.setProviderCallId(extractor.extract(json, IVRDataField.CALL_ID));
  
    String countryValue = extractor.extract(json, IVRDataField.COUNTRY);
    data.setCountryValue(countryValue);
    Country country = Country.UNKNOWN;
    if (!Strings.isNullOrEmpty(countryValue)) {
      country = getCountryCodeMap().get(data.getCountryValue());
      if (country == null) {
        errorList.add(new FieldError(FieldErrorType.INVALID_VALUE, IVRDataField.COUNTRY));
        country = Country.UNKNOWN;
      }
    }
    data.setCountry(country);
  
    data.setCallerNumber(extractor.extractPhone(country, json, IVRDataField.CALLER_NUMBER, true));
    data.setDialedNumber(extractor.extractPhone(country, json, IVRDataField.DIALED_NUMBER, true));
    data.setIvrNumber(extractor.extractPhone(country, json, IVRDataField.IVR_NUMBER, false));
    
    data.setKeyPress(extractor.extract(json, IVRDataField.KEY_PRESS, false));

    data.setNotificationUri(uri);
    if (errorList.isEmpty()) {
      data.setParseStatus(ParseStatus.SUCCESS);
    } else {
      data.setParseStatus(ParseStatus.FAILED);
      data.setErrorList(toParseErrorList(errorList));
    }
    data.setNotificationContent(json.toString(2));
    return data;
  }
  
  List<ParseError> toParseErrorList(List<FieldError> errorList) {
    List<ParseError> list = new ArrayList<>();
    for (FieldError error : errorList) {
      list.add(new ParseError(ParseErrorType.valueOf(error.getErrorType().name()),
          (IVRDataField) error.getField()));
    }
    return list;
  }
}
