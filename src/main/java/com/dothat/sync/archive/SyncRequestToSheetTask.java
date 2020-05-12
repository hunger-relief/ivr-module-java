package com.dothat.sync.archive;

import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.sheets.SheetsProvider;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * A Task that Syncs the Relief Request to a Google Sheet
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncRequestToSheetTask {
  private static final Logger logger = LoggerFactory.getLogger(SyncRequestToSheetTask.class);
  
  private static final String spreadsheetId = "1nYypj7tClNkDzmctfnpN1kKf9fiNN1236cvNrNPEeZI";
  
  private final ReliefRequest request;
  
  public SyncRequestToSheetTask(ReliefRequest request) {
    this.request = request;
  }
  
  public String executeRead() throws IOException, GeneralSecurityException {
    final String range = "Sync!A1:C1";
    logger.info("Ready to Read Range " + range);

    Sheets service = SheetsProvider.createSheetsService();
    logger.info("Created Sheets Service");
    
  
    ValueRange result = service.spreadsheets().values().get(spreadsheetId, range).execute();
    int numRows = 0;
    if (result != null) {
      logger.info("Read the Value Range " + result.getRange());
      numRows = result.getValues() != null ? result.getValues().size() : 0;
    } else {
      logger.info("Read returned a null value");
    }
    return "Read " + numRows + " from the range " + range;
  }
  
  public String executeWrite() throws IOException, GeneralSecurityException {
    final String range = "Sync!A1:C1";
    logger.info("Ready to Read Range " + range);
  
    Sheets service = SheetsProvider.createSheetsService();
    logger.info("Created Sheets Service");
  
    List<List<Object>> values = Arrays.asList(
        Arrays.asList("Thats", "the", "way")
    );
    ValueRange body = new ValueRange().setValues(values);
    UpdateValuesResponse result =
        service.spreadsheets().values().update(spreadsheetId, range, body)
            .setValueInputOption("RAW")
            .execute();
    logger.info("Executed the Raw Write");

    return result.getUpdatedCells().toString();
  }
}
