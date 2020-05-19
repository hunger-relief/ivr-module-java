package com.dothat.sync.sheets;

import com.dothat.profile.ProfileField;
import com.dothat.relief.request.field.RequestField;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lookup Row based on Search Criteria for a Sheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class LookupRow {
  private static final Logger logger = LoggerFactory.getLogger(LookupRow.class);

  private final Sheets service;
  private final String sheetName;
  private final List<String> fieldNames;
  
  public LookupRow(Sheets service, String sheetName, List<String> fieldNames) {
    this.service = service;
    this.sheetName = sheetName;
    this.fieldNames = fieldNames;
  }
  
  public static LookupRow forRequests(Sheets service) {
    return new LookupRow(service, "Requests!",
        Lists.newArrayList(RequestField.PHONE.name(), RequestField.REQUEST_DATE.name()));
  }
  
  public static LookupRow forProfiles(Sheets service) {
    return new LookupRow(service, "Profiles!",
        Lists.newArrayList(
            ProfileField.SOURCE_TYPE.name(),
            ProfileField.SOURCE.name(),
            ProfileField.SOURCE_ID.name()));
  }
  
  public LookupRowResult lookup(String spreadsheetId, SheetHeader header) throws IOException {
    List<String> ranges = composeRanges(header);

    BatchGetValuesResponse response = service.spreadsheets()
        .values()
        .batchGet(spreadsheetId)
        .setRanges(ranges)
        .setValueRenderOption("UNFORMATTED_VALUE")
        .setDateTimeRenderOption("SERIAL_NUMBER")
        .execute();
    List<ValueRange> rangeValueList = response.getValueRanges();
    List<List<List<Object>>> columnValuesList = new ArrayList<>();
    for (ValueRange rangeValue : rangeValueList) {
      columnValuesList.add(rangeValue.getValues());
    }
    return new LookupRowResult(columnValuesList, fieldNames);
  }
  
  private List<String> composeRanges(SheetHeader header) {
    List<String> ranges = new ArrayList<>();
    for (String fieldName : fieldNames) {
      String columnLabel = header.getColumnLabel(fieldName);
      if (columnLabel == null) {
        logger.error("No Column found for Field {} in the Sheet {}. Rows has Values {}",
            fieldName, sheetName, header.getRowValues());
        throw new IllegalStateException("No column found for Field " + fieldName + " in the Sheet "
            + sheetName + " It only has the following Columns " + header.getRowValues());
      }
      String range = sheetName + columnLabel + "1:" + columnLabel;
      logger.debug("Adding Range {} for Field {} in Row Lookup on Sheet {}", range, fieldName, sheetName);
      ranges.add(range);
    }
    return ranges;
  }
}
