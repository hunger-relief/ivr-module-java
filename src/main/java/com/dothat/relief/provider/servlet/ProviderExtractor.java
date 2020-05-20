package com.dothat.relief.provider.servlet;

import com.dothat.common.field.Field;
import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.field.error.FieldError;
import com.dothat.relief.provider.data.ReliefProvider;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extracts the Provider from a Registration Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
class ProviderExtractor {
  
  ReliefProvider extract(JSONObject json) {
    ReliefProvider data = new ReliefProvider();
    List<FieldError> errorList = new ArrayList<>();
    FieldValueExtractor extractor = new FieldValueExtractor(errorList);
    extractor.init(getFieldNameMap());
  
    data.setProviderCode(extractor.extract(json, ProviderField.CODE, true));

    return data;
  }
  
  private Map<Field, String> getFieldNameMap() {
    Map<Field, String> map = new HashMap<>();
    for (ProviderField field : ProviderField.values()) {
      map.put(field, field.getParamName());
    }
    return map;
  }
}
