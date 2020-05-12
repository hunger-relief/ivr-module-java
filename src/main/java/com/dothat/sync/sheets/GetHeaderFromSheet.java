package com.dothat.sync.sheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gets the first Row from a Sheet
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class GetHeaderFromSheet {
  private final Sheets service;
  
  public GetHeaderFromSheet(Sheets service) {
    this.service = service;
  }
  
  public List<String> getHeaders(String spreadsheetId, String sheetName) throws IOException {
    final String range = sheetName + "!A1:BZ1";
  
    ValueRange result = service.spreadsheets()
        .values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> rangeValues = result.getValues();
    if (rangeValues == null || rangeValues.size() == 0) {
      return null;
    }
    return toStringValues(rangeValues.get(0));
  }
  
  private List<String> toStringValues(List<Object> row) {
    List<String> values = new ArrayList<>();
    if (row != null) {
      for (Object cell : row) {
        values.add((String) cell);
      }
    }
    return values;
  }
}
