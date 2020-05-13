package com.dothat.sync.task;

import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ProviderConfig;
import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.sheets.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class BroadcastReliefRequest extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(BroadcastReliefRequest.class);
  
  public static final String REQUEST_ID_PARAM_NAME = "requestId";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String requestIdParam = req.getParameter(REQUEST_ID_PARAM_NAME);
    if (Strings.isNullOrEmpty(requestIdParam)) {
      logger.error("Relief Request Id not specified");
      resp.sendError(400, "Relief Request Id not specified");
      return;
    }
    Long requestId = Long.valueOf(requestIdParam);
    ReliefRequest request = new ReliefRequestService().lookupRequestById(requestId);
    if (request == null) {
      logger.error("No Request found for Id {} ", requestId);
      resp.sendError(404, "No Request found for Id " + requestId);
      return;
    }
//    ReliefRequest request = new SampleRequestGenerator()
//        .generate("+91-9899975887", RequestType.RATION);
    
    ProviderConfig config = new ReliefProviderService().getProviderConfig(request.getProvider());
    if (config == null || Strings.isNullOrEmpty(config.getGoogleSheetId())) {
      String providerCode = null;
      if (request.getProvider() != null) {
        providerCode = request.getProvider().getProviderCode();
      }
      resp.sendError(500,"No Spreadsheet found for provider " + providerCode);
      return;
    }
    Sheets sheets;
    try {
      sheets = SheetsProvider.createSheetsService();
    } catch (GeneralSecurityException gse) {
      logger.error("Error Reading Spreadsheet ", gse);
      throw new IOException(gse);
    }

    // TODO(abhideep): Decide between Requests and Shared Requests
    String sheetName = "Requests";
    List<String> fieldNames = new GetHeaderFromSheet(sheets)
        .getHeaders(config.getGoogleSheetId(), sheetName);
    List<List<Object>> values = new RequestRowComposer(fieldNames).compose(request, null);
    new AppendRowToSheet(sheets).appendRow(config.getGoogleSheetId(), AppendRowConfig.forRequest(), values);

    resp.setContentType("text/plain");
    resp.getWriter().println("Added 1 Row to the sheet " + sheetName);
    resp.flushBuffer();
  }
}
