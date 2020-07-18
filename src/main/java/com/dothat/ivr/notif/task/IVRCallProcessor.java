package com.dothat.ivr.notif.task;

import com.dothat.ivr.notif.IVRNotificationService;
import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.RelayMode;
import com.dothat.relief.request.data.ReliefRequest;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to receive Task Requests to Process Call Notifications.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRCallProcessor extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(IVRCallProcessor.class);
  
  public static final String CALL_ID_PARAM_NAME = "callId";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String callIdParam = req.getParameter(CALL_ID_PARAM_NAME);
    if (Strings.isNullOrEmpty(callIdParam)) {
      logger.error("Call Id not specified");
      resp.sendError(400, "Call Id not specified");
      return;
    }
    Long requestId;
    Long callId;
    try {
      callId = Long.valueOf(callIdParam);
      IVRCall call = new IVRNotificationService().lookupCallById(callId);
      if (call == null) {
        logger.error("No Call found for Id {} ", callId);
        resp.sendError(404, "No Call found for Id " + callId);
        return;
      }
      ReliefRequest data = new ReliefRequestGenerator().generate(call);
      requestId = new ReliefRequestService().save(data, toRelayMode(call.getUrgency()));
    } catch (Throwable t) {
      logger.error("Error while processing Call with Call Id {}", callIdParam, t);
      resp.sendError(500, "Error while processing Call with Call Id " + callIdParam);
      return;
    }
    resp.setContentType("text/plain");
    resp.getWriter().println("Created Relief Request with id " + requestId + " call with Id " + callId);
    resp.flushBuffer();
  }

  RelayMode toRelayMode(String urgency) {
    if (Strings.isNullOrEmpty(urgency)) {
      return null;
    }
    try {
      return RelayMode.valueOf(urgency.toUpperCase());
    } catch (Throwable t) {
      return null;
    }
  }

}
