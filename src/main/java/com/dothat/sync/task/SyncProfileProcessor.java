package com.dothat.sync.task;

import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.sync.SyncService;
import com.dothat.sync.data.SyncProcessType;
import com.dothat.sync.data.SyncProfileTask;
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
public class SyncProfileProcessor extends HttpServlet {
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
      logger.error("Sync type not specified");
      resp.sendError(400, "Sync type not specified");
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
    List<SyncProfileTask> taskList = syncService.getProfileTasks(taskName);
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

    String sheetName = "Profiles";
    List<String> fieldNames = new GetHeaderFromSheet(sheets)
        .getHeaders(destination.getGoogleSheetId(), sheetName);
    List<List<Object>> values = new ArrayList<>();
    ProfileRowComposer composer = new ProfileRowComposer(fieldNames);
    ProfileService service = new ProfileService();
    for (SyncProfileTask task : taskList) {
      ProfileAttribute attribute = task.getProfileAttribute();
      List<ProfileAttribute> attributes = service.lookupAllBySourceId(attribute.getIdentityUUID(),
          attribute.getSourceType(), attribute.getSource(), attribute.getSourceId());
      AttributeFieldExtractor extractor = new AttributeFieldExtractor(
          attribute.getIdentityUUID(), attributes);
      values.add(composer.composeRow(attribute, extractor));
    }
    
    new AppendRowToSheet(sheets)
        .appendRow(destination.getGoogleSheetId(), AppendRowConfig.forProfile(), values);

    // Delete the Tasks from the Data store
    syncService.deleteProfileTasks(taskList);
  
    int numRows = values.size();
    
    resp.setContentType("text/plain");
    resp.getWriter().println("Added " + numRows + " Rows to the Sheet " + sheetName + " of "
        + destination.getGoogleSheetId());
    resp.flushBuffer();
  }
}
