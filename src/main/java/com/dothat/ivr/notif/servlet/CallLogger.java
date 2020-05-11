package com.dothat.ivr.notif.servlet;

import com.dothat.ivr.notif.IVRNotificationService;
import com.dothat.ivr.notif.data.IVRCall;
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
import java.util.List;
import java.util.Map;

/**
 * Logs a Call or a Missed Call received by the system.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CallLogger extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(CallLogger.class);
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    String queryString = req.getQueryString();
    Map<String, List<String>> parameters = RequestParameterExtractor.extractFromGetRequest(queryString);
    JSONObject json = new JSONObject(RequestParameterExtractor.prepareForJson(parameters));
    processRequest(uri, json, resp);
  }
  
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    String contentType = req.getContentType();
    if (ContentType.TEXT_PLAIN.equals(contentType)) {
      Map<String, List<String>> parameters = RequestParameterExtractor.extractFromPostRequest(req);
      JSONObject json = new JSONObject(RequestParameterExtractor.prepareForJson(parameters));
      processRequest(uri, json, resp);
    } else if (ContentType.JSON.equals(contentType)) {
      JSONObject json = RequestParameterExtractor.extractFromJsonRequest(req);
      processRequest(uri, json, resp);
    }
  }
  
  private void processRequest(String uri, JSONObject json, HttpServletResponse resp)
      throws IOException {
    logger.info("Content received on URI " + uri + " : " + json.toString());
    IVRProvider provider = CallLogUrlRegistry.getInstance().getProvider(uri);
    IVRDataExtractor extractor = IVRDataExtractorRegistry.getInstance().getExtractor(provider);
    Long callId = null;
    if (extractor != null) {
      logger.info("Content received on URI " + uri + " will be processed by extractor for " + provider);
      // Extract and Save the call
      IVRCall call = extractor.extractCall(uri, json);
      callId = new IVRNotificationService().saveCall(call);
    }
    // Save the Call
    resp.setContentType("text/plain");
    resp.getWriter().println("Call Notification Received on uri " + uri + " and saved as Call " + callId);
    resp.flushBuffer();
  }
}
