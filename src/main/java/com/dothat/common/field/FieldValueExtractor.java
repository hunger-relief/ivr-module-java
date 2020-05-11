package com.dothat.common.field;

import com.dothat.common.field.error.FieldError;
import com.dothat.common.field.error.FieldErrorType;
import com.dothat.common.validate.PhoneNumberSanitizer;
import com.dothat.location.data.Country;
import com.google.common.base.Strings;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extracts data for a field.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class FieldValueExtractor {
  private static final Logger logger = LoggerFactory.getLogger(FieldValueExtractor.class);

  private final Map<Field, String> fieldNameMap = new HashMap<>();
  private final List<FieldError> errorList;

  public FieldValueExtractor(List<FieldError> errorList) {
    this.errorList = errorList;
  }

  public void init(Map<Field, String> newFieldNameMap) {
    fieldNameMap.clear();
    if (newFieldNameMap != null) {
      for (Field field : newFieldNameMap.keySet()) {
        fieldNameMap.put(field, newFieldNameMap.get(field));
      }
    }
  }

  public Map<Field, String> getFieldNameMap() {
    return fieldNameMap;
  }

  public String extract(JSONObject json, Field field) {
    return extract(json, field, true);
  }

  public String extract(JSONObject json, Field field, boolean isRequired) {
    String fieldName = getFieldNameMap().get(field);
    if (Strings.isNullOrEmpty(fieldName)) {
      errorList.add(new FieldError(FieldErrorType.MISSING_FIELD_CONFIG, field));
      logger.error("No field name defined for {}", field.getFieldName());
      return null;
    }

    try {
      String value = null;
      if (json.has(fieldName)) {
        value = json.optString(fieldName, null);
      }
      
      if (value == null) {
        logger.info("Field {} has a null value in json {}", fieldName, json);
      }

      if (Strings.isNullOrEmpty(value) && isRequired) {
        errorList.add(new FieldError(FieldErrorType.MISSING_VALUE, field));
        logger.error("No value retrieved for {} from {}", field.getFieldName(), json);
      }
      return value;
    } catch (JSONException je) {
      if (isRequired) {
        errorList.add(new FieldError(FieldErrorType.MISSING_VALUE, field));
        logger.error("Error retrieving {} from {}", field.getFieldName(), json);
      }
      return null;
    }
  }
  
  public String extractPhone(Country country, JSONObject json, Field field, boolean isRequired) {
    String phone = extract(json, field, isRequired);
    if (Strings.isNullOrEmpty(phone)) {
      // Error was already added above.
      return phone;
    }
    if (country == null) {
      errorList.add(new FieldError(FieldErrorType.CANNOT_PARSE, field));
      return phone;
    }
    PhoneNumberSanitizer sanitizer = new PhoneNumberSanitizer(country);
    try {
      return sanitizer.sanitize(phone);
    } catch (IllegalStateException ise) {
      errorList.add(new FieldError(FieldErrorType.CONFIG_ERROR, field));
      return phone;
    }
  }

}
