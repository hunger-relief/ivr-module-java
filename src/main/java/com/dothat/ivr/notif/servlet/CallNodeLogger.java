package com.dothat.ivr.notif.servlet;

import com.dothat.common.request.RequestDataExtractor;
import com.dothat.ivr.notif.IVRNotificationService;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.ivr.notif.data.IVRProvider;
import com.dothat.ivr.notif.extractor.IVRDataExtractor;
import com.dothat.ivr.notif.extractor.IVRDataExtractorRegistry;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logs a Node Visited on an IVR call.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CallNodeLogger extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(CallNodeLogger.class);
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    JSONObject json = new RequestDataExtractor().extractGet(req);
    processRequest(uri, json, resp);
  }
  
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    JSONObject json = new RequestDataExtractor().extractPost(req);
    processRequest(uri, json, resp);
  }
  
  private void processRequest(String uri, JSONObject json, HttpServletResponse resp)
      throws IOException {
    logger.info("Content received on URI " + uri + " : " + json.toString());
    IVRProvider provider = CallLogUrlRegistry.getInstance().getProvider(uri);
    IVRDataExtractor extractor = IVRDataExtractorRegistry.getInstance().getExtractor(provider);
    Long callNodeId = null;
    if (extractor != null) {
      logger.info("Content received on URI " + uri + " will be processed by extractor for " + provider);
      // Extract and Save the call
      IVRCallNode node = extractor.extractCallNode(uri, json);
      callNodeId = new IVRNotificationService().saveCallNode(node);
    }
    // Save the Call
    resp.setContentType("text/plain");
    resp.getWriter().println("Call Notification Received on uri " + uri + " and saved as Call Node " + callNodeId);
    resp.flushBuffer();
  }
}
