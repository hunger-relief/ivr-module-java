package com.dothat.sync.task;

import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.destination.DestinationService;
import com.dothat.sync.destination.data.DestinationType;
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
  
    // TODO(abhideep): Lookup all Providers who need to be sent this broadcast.
    Destination destination = new DestinationService()
        .lookupDestination(request.getProvider(), request.getRequestType(), request.getLocation(),
            DestinationType.GOOGLE_SHEETS);
    if (destination == null || Strings.isNullOrEmpty(destination.getGoogleSheetId())) {
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
        .getHeaders(destination.getGoogleSheetId(), sheetName);
    List<List<Object>> values = new RequestRowComposer(fieldNames)
        .compose(request, new AttributeFieldExtractor(request.getRequesterID()));
    new AppendRowToSheet(sheets)
        .appendRow(destination.getGoogleSheetId(), AppendRowConfig.forRequest(), values);

    resp.setContentType("text/plain");
    resp.getWriter().println("Added 1 Row to the sheet " + sheetName);
    resp.flushBuffer();
  }
}
