package com.dothat.sync.task;

import com.dothat.relief.request.RequestSorter;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.SyncService;
import com.dothat.sync.data.SyncProcessType;
import com.dothat.sync.data.SyncRequestTask;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.sheets.AppendRowConfig;
import com.dothat.sync.sheets.AppendRowToSheet;
import com.dothat.sync.sheets.GetHeaderFromSheet;
import com.dothat.sync.sheets.SheetsProvider;
import com.google.api.services.sheets.v4.Sheets;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Updates the Google Sheet with all the Requests that needs to downloaded.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncRequestProcessor extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(BroadcastReliefRequest.class);
  
  public static final String PROVIDER_CODE_PARAM_NAME = "providerCode";
  public static final String SYNC_TYPE_PARAM_NAME = "syncType";
  public static final String PROCESS_TASK_PARAM_NAME = "processTaskName";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String providerCode = req.getParameter(PROVIDER_CODE_PARAM_NAME);
    if (Strings.isNullOrEmpty(providerCode)) {
      logger.error("Relief Provider code not specified");
      resp.sendError(400, "Relief Provider code not specified");
      return;
    }
    String syncTypeValue = req.getParameter(SYNC_TYPE_PARAM_NAME);
    if (Strings.isNullOrEmpty(syncTypeValue)) {
      logger.error("Relief Request type not specified");
      resp.sendError(400, "Relief Request type not specified");
      return;
    }
    SyncProcessType syncType;
    try {
      syncType = SyncProcessType.valueOf(syncTypeValue);
    } catch (Throwable t) {
      logger.error("Invalid Sync type specified {}", syncTypeValue);
      resp.sendError(400, "Invalid Sync type specified " + syncTypeValue);
      return;
    }
    String taskName = req.getParameter(PROCESS_TASK_PARAM_NAME);
  
    SyncService syncService = new SyncService();
    List<SyncRequestTask> taskList = syncService.getRequestTasks(taskName);
    if (taskList == null || taskList.isEmpty()) {
      resp.setContentType("text/plain");
      resp.getWriter().println("No rows to process for Sheet for " + syncType
          + " requests for " + providerCode + " for Task " + taskName);
      resp.flushBuffer();
      return;
    }

    // All the Tasks have the same destination
    Destination destination = taskList.get(0).getDestination();
  
    Sheets sheets;
    try {
      sheets = SheetsProvider.createSheetsService();
    } catch (GeneralSecurityException gse) {
      logger.error("Error Reading Spreadsheet ", gse);
      throw new IOException(gse);
    }

    String sheetName = destination.getGoogleSheetName();
    if (destination.getGoogleSheetName() == null || destination.getGoogleSheetName().isEmpty()) {
      sheetName = "Requests";
    }
    List<String> fieldNames = new GetHeaderFromSheet(sheets)
        .getHeaders(destination.getGoogleSheetId(), sheetName);
    List<List<Object>> values = new ArrayList<>();
    RequestRowComposer composer = new RequestRowComposer(fieldNames);
    List<ReliefRequest> requestList = new ArrayList<>();
    for (SyncRequestTask task : taskList) {
      requestList.add(task.getReliefRequest());
    }
    requestList.sort(new RequestSorter(true));
    for (ReliefRequest request : requestList) {
      values.add(composer.composeRow(request, new AttributeFieldExtractor(request.getRequesterID())));
    }
    
    // Append the Rows to the Google Sheet
    new AppendRowToSheet(sheets)
        .appendRow(destination.getGoogleSheetId(), AppendRowConfig.forRequest(sheetName), values);
    // Delete the Tasks from the Data store
    syncService.deleteRequestTasks(taskList);
  
    int numRows = values.size();
    
    resp.setContentType("text/plain");
    resp.getWriter().println("Added " + numRows + " Rows to the Sheet " + sheetName + " of "
        + destination.getGoogleSheetId());
    resp.flushBuffer();
  }
}
