package com.dothat.sync.destination.servlet;

import com.dothat.common.field.Field;
import com.dothat.common.field.FieldValueExtractor;
import com.dothat.common.field.error.FieldError;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.destination.data.DestinationType;
import com.google.common.base.Strings;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extracts the Destination from a Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
class DestinationExtractor {
  private static final Logger logger = LoggerFactory.getLogger(DestinationExtractor.class);

  Destination extract(JSONObject json) {
    Destination data = new Destination();
    List<FieldError> errorList = new ArrayList<>();
    FieldValueExtractor extractor = new FieldValueExtractor(errorList);
    extractor.init(getFieldNameMap());
  
    ReliefProvider provider = new ReliefProvider();
    provider.setProviderCode(extractor.extract(json, DestinationField.PROVIDER, true));
    data.setProvider(provider);
    try {
      data.setRequestType(extractor.extractEnum(
          json, DestinationField.REQUEST_TYPE, RequestType.class, false));
    } catch (Throwable t) {
      throw new IllegalArgumentException("Invalid Destination Type");
    }
    try {
      data.setDestinationType(extractor.extractEnum(
          json, DestinationField.DESTINATION_TYPE, DestinationType.class, true));
    } catch (Throwable t) {
      throw new IllegalArgumentException("Invalid Destination Type");
    }
    data.setGoogleSheetId(extractor.extract(json, DestinationField.GOOGLE_SHEET_ID, true));
    data.setGoogleSheetName(extractor.extract(json, DestinationField.GOOGLE_SHEET_NAME, false));
    String syncFrequency = extractor.extract(json, DestinationField.SYNC_FREQ_SECS, false);
    logger.warn("Sync Frequency in the Request is {}", syncFrequency);
    if (syncFrequency != null) {
      try {
        data.setSyncFrequencyInSeconds(Integer.valueOf(syncFrequency));
        logger.warn("Saving Sync Frequency is {}", data.getSyncFrequencyInSeconds());
      } catch (Throwable t) {
        throw new IllegalArgumentException("Invalid Sync Frequency");
      }
    }

    String locationId = extractor.extract(json, DestinationField.LOCATION, false);
    if (!Strings.isNullOrEmpty(locationId)) {
      Location location = new Location();
      data.setLocation(location);
      location.setLocationId(Long.valueOf(locationId));
    }
    return data;
  }
  
  private Map<Field, String> getFieldNameMap() {
    Map<Field, String> map = new HashMap<>();
    for (DestinationField field : DestinationField.values()) {
      map.put(field, field.getParamName());
    }
    return map;
  }
}
