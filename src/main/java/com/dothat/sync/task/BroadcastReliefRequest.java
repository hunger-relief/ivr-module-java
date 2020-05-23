package com.dothat.sync.task;

import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ReliefProvider;
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
  public static final String REQUEST_UUID_PARAM_NAME = "requestUUID";
  public static final String REQUEST_SOURCE_ID_PARAM_NAME = "requestSourceId";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String requestIdValue = req.getParameter(REQUEST_ID_PARAM_NAME);
    if (Strings.isNullOrEmpty(requestIdValue)) {
      logger.error("Relief Request Id not specified");
      resp.sendError(400, "Relief Request Id not specified");
      return;
    }
    Long requestId = Long.valueOf(requestIdValue);
    ReliefRequest request = new ReliefRequestService().lookupRequestById(requestId);
    if (request == null) {
      String requestUUID = req.getParameter(REQUEST_UUID_PARAM_NAME);
      String requestSourceId = req.getParameter(REQUEST_SOURCE_ID_PARAM_NAME);
      logger.error("No Request found with Id {} from {} with Source ID {} ",
          requestIdValue, requestUUID, requestSourceId);
      resp.sendError(404, "No Request found with Id " + requestIdValue
          + " from " + requestUUID + " with Source ID " + requestSourceId);
      return;
    }
//    ReliefRequest request = new SampleRequestGenerator()
//        .generate("+91-9899975887", RequestType.RATION);
  
    // TODO(abhideep): Lookup all Providers who need to be sent this broadcast.

    // Load the Provider if needed
    ReliefProvider provider = request.getProvider();
    if (provider != null && provider.getProviderId() == null && provider.getProviderCode() != null) {
      provider = new ReliefProviderService().lookupByCode(provider.getProviderCode());
    }
    Destination destination = new DestinationService()
        .lookupDestination(provider, request.getRequestType(), request.getLocation(),
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
