package com.dothat.sync.sheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get a Row of Values from a Sheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class LookupSheetHeader {
  private final Sheets service;
  private final String spreadsheetId;
  private final String rowRange;
  
  public LookupSheetHeader(Sheets service, String spreadsheetId, String rowRange) {
    this.service = service;
    this.spreadsheetId = spreadsheetId;
    this.rowRange = rowRange;
  }
  
  public static LookupSheetHeader forProfiles(Sheets service, String spreadsheetId) {
    return new LookupSheetHeader(service, spreadsheetId, "Profiles!A1:AZ1");
  }

  public static LookupSheetHeader forRequests(Sheets service, String spreadsheetId, String sheetName) {
    String sheetRange = sheetName + "!A1:AZ1";
    return new LookupSheetHeader(service, spreadsheetId, sheetRange);
  }
  
  public SheetHeader getHeader() throws IOException {
    ValueRange result = service.spreadsheets()
        .values()
        .get(spreadsheetId, rowRange)
        .execute();
    List<List<Object>> rangeValues = result.getValues();
    if (rangeValues == null || rangeValues.size() == 0) {
      return new SheetHeader(new ArrayList<>());
    }
    return new SheetHeader(rangeValues.get(0));
  }
}
