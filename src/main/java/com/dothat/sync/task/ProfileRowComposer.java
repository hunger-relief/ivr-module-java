package com.dothat.sync.task;

import com.dothat.profile.ProfileFieldExtractor;
import com.dothat.profile.data.ProfileAttribute;

import java.util.*;

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
  
  public List<List<Object>> compose(ProfileAttribute data, List<ProfileAttribute> attributeList) {
    Map<String, String> fieldMap = new ProfileFieldExtractor().extractValues(data);

    // TODO(abhideep) : Process Attributes as well.
    Map<String, String> attributeMap = new HashMap<>();

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
    return Collections.singletonList(row);
  }
}
