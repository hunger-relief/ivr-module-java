package com.dothat.sync.sheets;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ExternalID;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.field.ReliefRequestFieldExtractor;
import com.dothat.relief.request.field.RequestField;
import org.joda.time.DateTime;

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
  
  public List<List<Object>> compose(ReliefRequest data, List<ProfileAttribute> attributeList) {
    ExternalID number = new IdentityService().lookupNumberById(data.getRequesterID());
    String phoneNUmber = number ==  null ? "" : number.getExternalId();
    // Strip out the country code if any and add ' to make it a string literal
    int index = phoneNUmber.indexOf("-");
    phoneNUmber = "'" + phoneNUmber.substring(index + 1);
    
    Map<String, String> fieldMap = new ReliefRequestFieldExtractor().generateFieldValueMap(
        phoneNUmber, data);

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
    return Arrays.asList(row);
  }
}
