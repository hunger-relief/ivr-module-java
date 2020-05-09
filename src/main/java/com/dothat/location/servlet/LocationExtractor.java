package com.dothat.location.servlet;

import com.dothat.common.field.Field;
import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.field.error.FieldError;
import com.dothat.location.StateLookup;
import com.dothat.location.data.Country;
import com.dothat.location.data.Location;
import com.google.common.base.Strings;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extracts the location from a Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
class LocationExtractor {
  
  Location extract(JSONObject json) {
    Location data = new Location();
    List<FieldError> errorList = new ArrayList<>();
    FieldValueExtractor extractor = new FieldValueExtractor(errorList);
    extractor.init(getFieldNameMap());
    
    data.setLocation(extractor.extract(json, LocationField.LOCATION, false));
    data.setArea(extractor.extract(json, LocationField.AREA, false));
    data.setZone(extractor.extract(json, LocationField.ZONE, false));
    data.setCity(extractor.extract(json, LocationField.CITY, false));
    
    String countryValue = extractor.extract(json, LocationField.COUNTRY);
    Country country = null;
    if (!Strings.isNullOrEmpty(countryValue)) {
      country = Country.valueOf(countryValue);
      data.setCountry(country);
    }
  
    String stateValue = extractor.extract(json, LocationField.STATE);
    if (country != null && !Strings.isNullOrEmpty(stateValue)) {
      data.setState(StateLookup.getInstance().getEnum(country, stateValue));
    }
    return data;
  }
  
  private Map<Field, String> getFieldNameMap() {
    Map<Field, String> map = new HashMap<>();
    for (LocationField field : LocationField.values()) {
      map.put(field, field.getParamName());
    }
    return map;
  }
}
