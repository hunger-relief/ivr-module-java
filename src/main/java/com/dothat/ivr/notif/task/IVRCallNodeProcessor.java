package com.dothat.ivr.notif.task;

import com.dothat.ivr.notif.IVRNotificationService;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to receive Task Requests to Process Call Node Notifications.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRCallNodeProcessor extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(IVRCallNodeProcessor.class);
  
  public static final String CALL_NODE_ID_PARAM_NAME = "callNodeId";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String callNodeIdParam = req.getParameter(CALL_NODE_ID_PARAM_NAME);
    if (Strings.isNullOrEmpty(callNodeIdParam)) {
      logger.error("Call Node Id not specified");
      resp.sendError(400, "Call Node Id not specified");
      return;
    }
    Long attributeId = null;
    Long callNodeId = null;
    try {
      callNodeId = Long.valueOf(callNodeIdParam);
      IVRCallNode node = new IVRNotificationService().lookupNodeById(callNodeId);
      if (node == null) {
        logger.error("No Call Node found for Id {} ", callNodeId);
        resp.sendError(404, "No Call Node found for Id " + callNodeId);
        return;
      }
      ProfileAttribute data = new ProfileAttributeGenerator().generate(node);
      attributeId = new ProfileService().save(data);
    } catch (Throwable t) {
      logger.error("Error while processing Call Node with Id {}", callNodeIdParam, t);
      resp.sendError(500, "Error while processing Call Node with Id " + callNodeIdParam);
      return;
    }
    resp.setContentType("text/plain");
    resp.getWriter().println("Created Attribute with id " + attributeId + " for call node with Id " + callNodeId);
    resp.flushBuffer();
  }
}
