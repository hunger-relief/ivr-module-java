package com.dothat.sync.task;

import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ExternalID;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.field.ReliefRequestFieldExtractor;

import java.util.*;

/**
 * Generates the data for a Spreadsheet Row based on the field names in the first row of the SpreadSheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestRowComposer {
  private final List<String> fieldNames;
  
  public RequestRowComposer(List<String> fieldNames) {
    this.fieldNames = fieldNames;
  }
  
  public List<List<Object>> compose(ReliefRequest data, AttributeFieldExtractor attributeExtractor) {
    ExternalID number = new IdentityService().lookupNumberById(data.getRequesterID());
    String phoneNUmber = number ==  null ? "" : number.getExternalId();
    // Strip out the country code if any and add ' to make it a string literal
    int index = phoneNUmber.indexOf("-");
    phoneNUmber = "'" + phoneNUmber.substring(index + 1);
    
    Map<String, String> fieldMap = new ReliefRequestFieldExtractor().generateFieldValueMap(
        phoneNUmber, data);
  
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
    return Collections.singletonList(row);
  }
}
