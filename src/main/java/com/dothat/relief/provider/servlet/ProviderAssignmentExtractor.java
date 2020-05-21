package com.dothat.relief.provider.servlet;

import com.dothat.common.field.Field;
import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.field.error.FieldError;
import com.dothat.location.data.Country;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ProviderAssignment;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestSource;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import com.google.common.base.Strings;
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
class ProviderAssignmentExtractor {
  
  ProviderAssignment extract(JSONObject json) {
    ProviderAssignment data = new ProviderAssignment();
    List<FieldError> errorList = new ArrayList<>();
    FieldValueExtractor extractor = new FieldValueExtractor(errorList);
    extractor.init(getFieldNameMap());
  
    ReliefProvider provider = new ReliefProvider();
    provider.setProviderCode(extractor.extract(json, ProviderAssignmentField.PROVIDER, true));
    data.setProvider(provider);

    data.setRequestType(extractor.extractEnum(json, ProviderAssignmentField.REQUEST_TYPE, RequestType.class,
        false));
  
    RequestSource source = new RequestSource();
    source.setDialedNumber(extractor.extract(json, ProviderAssignmentField.PHONE, false));
    source.setCountry(extractor.extractEnum(json, ProviderAssignmentField.COUNTRY, Country.class,
        false));
    source.setSourceType(extractor.extractEnum(json, ProviderAssignmentField.SOURCE_TYPE, SourceType.class,
        false));
    source.setSource(extractor.extract(json, ProviderAssignmentField.SOURCE, false));

    if (!Strings.isNullOrEmpty(source.getDialedNumber()) || source.getSourceType() != null ||
        !Strings.isNullOrEmpty(source.getSource()) || source.getCountry() != null) {
      data.setSource(source);
    }

    String locationId = extractor.extract(json, ProviderAssignmentField.LOCATION_ID, false);
    if (!Strings.isNullOrEmpty(locationId)) {
      data.setLocation(new Location());
      data.getLocation().setLocationId(Long.valueOf(locationId));
    }
    return data;
  }
  
  private Map<Field, String> getFieldNameMap() {
    Map<Field, String> map = new HashMap<>();
    for (ProviderAssignmentField field : ProviderAssignmentField.values()) {
      map.put(field, field.getParamName());
    }
    return map;
  }
}
