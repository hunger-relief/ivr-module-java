package com.dothat.ivr.mapping.servlet;

import com.dothat.common.request.RequestDataExtractor;
import com.dothat.ivr.mapping.IVRMappingService;
import com.dothat.ivr.mapping.data.IVRMapping;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Receives the request to configure a location and map it to a Service and Location.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRMappingServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(IVRMappingServlet.class);
  
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
  
  private void processRequest(String uri, JSONObject json, HttpServletResponse resp) throws IOException {
    logger.info("Received IVR Mapping Request on URI " + uri + " : " + json.toString());
    String message = "IVR Mapping Request registered with Id ";
    try {
      IVRMapping data = new IVRMappingExtractor().extract(json);
      Long mappingId = new IVRMappingService().register(data);
      message = message + mappingId;
    } catch (IllegalArgumentException iae) {
      message = iae.getMessage();
    }
    // Save the Call
    resp.setContentType("text/plain");
    resp.getWriter().println(message);
    resp.flushBuffer();
  
  }
}
