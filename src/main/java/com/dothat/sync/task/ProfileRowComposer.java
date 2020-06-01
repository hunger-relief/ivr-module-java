package com.dothat.sync.task;

import com.dothat.profile.ProfileFieldExtractor;
import com.dothat.profile.data.ProfileAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Generates the data for a Spreadsheet Row based on the field names in the first row of the SpreadSheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileRowComposer {
  private final List<String> fieldNames;
  
  public ProfileRowComposer(List<String> fieldNames) {
    this.fieldNames = fieldNames;
  }
  
  public List<List<Object>> compose(ProfileAttribute data, AttributeFieldExtractor attributeExtractor) {
    return Collections.singletonList(composeRow(data, attributeExtractor));
  }
  
  public List<Object> composeRow(ProfileAttribute data, AttributeFieldExtractor attributeExtractor) {
    Map<String, String> fieldMap = new ProfileFieldExtractor().extractValues(data);

    Map<String, String> attributeMap = attributeExtractor.getAttributeDataMap();

    List<Object> row = new ArrayList<>();
    for (String fieldName : fieldNames) {
      if (fieldMap.containsKey(fieldName)) {
        String fieldValue = fieldMap.get(fieldName);
        row.add(fieldValue == null ? "" : fieldValue);
      } else if (attributeMap.containsKey(fieldName)) {
        String fieldValue = attributeMap.get(fieldName);
        row.add(fieldValue == null ? "" : fieldValue);
      } else {
        row.add("");
      }
    }
    return row;
  }
}
