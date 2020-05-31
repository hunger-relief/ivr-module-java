package com.dothat.sync.task;

import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.SyncService;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestBroadcastProcessor extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(RequestBroadcastProcessor.class);
  
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
    String requestUUID = req.getParameter(REQUEST_UUID_PARAM_NAME);
    String requestSourceId = req.getParameter(REQUEST_SOURCE_ID_PARAM_NAME);

    Long requestId = Long.valueOf(requestIdValue);
    ReliefRequest request = new ReliefRequestService().lookupRequestById(requestId);
    if (request == null) {
      logger.error("No Request found with Id {} from {} with Source ID {} ",
          requestIdValue, requestUUID, requestSourceId);
      resp.sendError(404, "No Request found with Id " + requestIdValue
          + " from " + requestUUID + " with Source ID " + requestSourceId);
      return;
    }
    
    Long taskId = new SyncService().createSyncTask(request);

    resp.setContentType("text/plain");
    resp.getWriter().println("Created Sync Task " + taskId
        + " to request with Id " + requestIdValue
        + " from " + requestUUID
        + " with Source ID " + requestSourceId );
    resp.flushBuffer();
  }
}
