package com.dothat.sync.task;

import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.SyncService;
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
  public static final String REQUEST_TYPE_PARAM_NAME = "requestType";
  public static final String PROCESS_TASK_PARAM_NAME = "processTaskName";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String providerCode = req.getParameter(PROVIDER_CODE_PARAM_NAME);
    if (Strings.isNullOrEmpty(providerCode)) {
      logger.error("Relief Provider code not specified");
      resp.sendError(400, "Relief Provider code not specified");
      return;
    }
    String requestTypeValue = req.getParameter(REQUEST_TYPE_PARAM_NAME);
    if (Strings.isNullOrEmpty(requestTypeValue)) {
      logger.error("Relief Request type not specified");
      resp.sendError(400, "Relief Request type not specified");
      return;
    }
    RequestType requestType;
    try {
      requestType = RequestType.valueOf(requestTypeValue);
    } catch (Throwable t) {
      logger.error("Invalid Relief Request type specified {}", requestTypeValue);
      resp.sendError(400, "Invalid Relief Request type specified " + requestTypeValue);
      return;
    }
    String taskName = req.getParameter(PROCESS_TASK_PARAM_NAME);
  
    List<SyncRequestTask> taskList = new SyncService().getTasks(taskName);
    if (taskList == null || taskList.isEmpty()) {
      resp.setContentType("text/plain");
      resp.getWriter().println("No rows to process for Sheet for " + requestType
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

    String sheetName = "Requests";
    List<String> fieldNames = new GetHeaderFromSheet(sheets)
        .getHeaders(destination.getGoogleSheetId(), sheetName);
    List<List<Object>> values = new ArrayList<>();
    RequestRowComposer composer = new RequestRowComposer(fieldNames);
    for (SyncRequestTask task : taskList) {
      ReliefRequest request = task.getReliefRequest();
      values.add(composer.composeRow(request, new AttributeFieldExtractor(request.getRequesterID())));
    }
    
    new AppendRowToSheet(sheets)
        .appendRow(destination.getGoogleSheetId(), AppendRowConfig.forRequest(), values);
  
    int numRows = values.size();
    
    resp.setContentType("text/plain");
    resp.getWriter().println("Added " + numRows + " Rows to the Sheet " + sheetName + " of "
        + destination.getGoogleSheetId());
    resp.flushBuffer();
  }
}
