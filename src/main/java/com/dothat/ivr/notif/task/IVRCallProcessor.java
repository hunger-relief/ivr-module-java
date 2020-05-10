package com.dothat.ivr.notif.task;

import com.dothat.ivr.notif.IVRNotificationService;
import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.ReliefRequest;
import com.google.common.base.Strings;

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
  
  public static final String CALL_ID_PARAM_NAME = "callId";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String callIdParam = req.getParameter(CALL_ID_PARAM_NAME);
    if (Strings.isNullOrEmpty(callIdParam)) {
      resp.sendError(400, "Call Id not specified");
      return;
    }
    try {
      Long callId = Long.valueOf(callIdParam);
      IVRCall call = new IVRNotificationService().lookupCallById(callId);
      if (call == null) {
        resp.sendError(404, "No Call found for Id " + callId);
        return;
      }
      ReliefRequest data = new ReliefRequestGenerator().generate(call);
      Long requestId = new ReliefRequestService().save(data);
    } catch (Throwable t) {
      resp.sendError(500, "Invalid Call Processoe with Call Id " + callIdParam);
    }
  }
}
