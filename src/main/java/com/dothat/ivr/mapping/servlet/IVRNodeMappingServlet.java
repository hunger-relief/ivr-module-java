package com.dothat.ivr.mapping.servlet;

import com.dothat.common.request.RequestDataExtractor;
import com.dothat.ivr.mapping.IVRNodeMappingService;
import com.dothat.ivr.mapping.data.IVRNodeMapping;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Receives the request to configure an IVR node to the value associated with it.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRNodeMappingServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(IVRNodeMappingServlet.class);
  
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
    logger.info("Received IVR Node Mapping Request on URI " + uri + " : " + json.toString());
    String message = "IVR Node Mapping Request registered with Id ";
    try {
      IVRNodeMapping data = new IVRNodeMappingExtractor().extract(json);
      Long mappingId = new IVRNodeMappingService().registerNode(data);
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
