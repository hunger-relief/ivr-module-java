package com.dothat.sync.sheets;

import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ExternalID;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.profile.data.ProfileAttribute;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class UpdateCellTask {
  private static final Logger logger = LoggerFactory.getLogger(UpdateCellTask.class);
  
  private final Sheets service;

  public UpdateCellTask(Sheets service) {
    this.service = service;
  }
  
  public void updateCell(String spreadsheetId, UpdateCellTaskConfig config, ProfileAttribute attribute) throws IOException {
    ObfuscatedID obfuscatedId = attribute.getIdentityUUID();
    ExternalID number = new IdentityService().lookupNumberById(obfuscatedId);
    String phoneNumber = "";
    if (number != null) {
      phoneNumber = number.getExternalId();
    }

    List<List<Object>> values = new ArrayList<>();
    values.add(Collections.singletonList(SheetDataSanitizer.sanitizePhoneNumber(phoneNumber)));
    values.add(Collections.singletonList(obfuscatedId == null ? "" : obfuscatedId.getIdentifier()));
    values.add(Collections.singletonList(attribute.getAttributeName()));
  
    ValueRange body = new ValueRange().setValues(values);
    service.spreadsheets().values().update(spreadsheetId, config.getLookupSheetRange(), body)
        .setValueInputOption("USER_ENTERED")
        .execute();
    
    // First Lookup the Row and Column that needs to be
    // TODO(abhideep): Lookup entire column toa void race conditions
  
    ValueRange result = service.spreadsheets()
        .values()
        .get(spreadsheetId, config.getRowColRange())
        .execute();
    List<List<Object>> rangeValues = result.getValues();
    if (rangeValues == null || rangeValues.size() == 0) {
      return;
    }
    String rowLabel = (String) rangeValues.get(0).get(0);
    String columnValue = (String) rangeValues.get(1).get(0);
    if (!(isNumber(rowLabel) && isNumber(columnValue))) {
      logger.error("Phone number " + phoneNumber + " not found in Sheet " + config.getSheetName());
      // Skip the update if the row is not found
      return;
    }

    String columnLabel;
    Integer column = Integer.valueOf(columnValue);
    if (column < 27) {
      columnLabel = String.valueOf((char)(column + 64));
    } else {
      int firstChar = column / 26;
      int secondChar = column - (firstChar * 26);
      columnLabel = String.valueOf((char)(firstChar + 64)) + String.valueOf((char)(secondChar + 64));
    }
    String cellRange = config.getSheetName() + columnLabel + rowLabel;
    cellRange = cellRange.trim();
  
    logger.info("Updating Attribute " + attribute.getAttributeName() + " for Phone number " + phoneNumber
        + " is cell " + cellRange);
    
    values = new ArrayList<>();
    values.add(Collections.singletonList(attribute.getAttributeValue()));
  
    ValueRange cell = new ValueRange().setValues(values);
    service.spreadsheets().values().update(spreadsheetId, cellRange, cell)
        .setValueInputOption("USER_ENTERED")
        .execute();
  }
  
  private boolean isNumber(String value) {
    try {
      Integer.valueOf(value);
    } catch (Throwable t) {
      return false;
    }
    return true;
  }
}
