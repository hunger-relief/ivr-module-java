package com.dothat.sync.sheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.List;

/**
 * Appends a Row to the Given Sheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class AppendRowToSheet {
  
  private final Sheets service;
  
  public AppendRowToSheet(Sheets service) {
    this.service = service;
  }
  
  public void appendRow(String spreadsheetId, AppendRowConfig config,
                        List<List<Object>> values) throws IOException {
    String lookupRange = config.getSheetName() + config.getLookupSheetRange();
    ValueRange body = new ValueRange().setValues(values);
    service.spreadsheets().values().append(spreadsheetId, lookupRange, body)
        .setValueInputOption("USER_ENTERED")
        .execute();
  }
}
