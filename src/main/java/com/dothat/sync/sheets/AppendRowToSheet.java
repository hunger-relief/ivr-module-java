package com.dothat.sync.sheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
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
  
  public void appendRow(String spreadsheetId, String sheetName, List<List<Object>> values) throws IOException {
    final String range = sheetName + "!A1:D";
    ValueRange result = service.spreadsheets().values().get(spreadsheetId, range).execute();
    int numRows = result.getValues() != null ? result.getValues().size() : 0;
    String newRow = String.valueOf(numRows + 1);
  
    final String newRowRange = sheetName + "!A" + newRow + ":Z" + newRow;

    ValueRange body = new ValueRange().setValues(values);
    UpdateValuesResponse updateResult =
        service.spreadsheets().values().update(spreadsheetId, newRowRange, body)
            .setValueInputOption("USER_ENTERED")
            .execute();
//    List<List<Object>> dateCell = Lists.newArrayList();
//    dateCell.add(Lists.newArrayList(values.get(0).get(0)));
//    ValueRange dateValue = new ValueRange().setValues(dateCell);
//
//    final String dateCellRange = sheetName + "!A" + newRow;
//    UpdateValuesResponse updateDateResult =
//        service.spreadsheets().values().update(spreadsheetId, dateCellRange, dateValue)
//            .setValueInputOption("USER_ENTERED")
//            .execute();
  }
}
