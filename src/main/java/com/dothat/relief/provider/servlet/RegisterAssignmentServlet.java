package com.dothat.relief.provider.servlet;

import com.dothat.common.request.RequestDataExtractor;
import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ProviderAssignment;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to register or update a Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RegisterAssignmentServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(RegisterAssignmentServlet.class);
  
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
    String message = "Provider Assignment registered with Id ";
    try {
      ProviderAssignment data = new ProviderAssignmentExtractor().extract(json);
      Long assignmentId = new ReliefProviderService().registerAssignment(data);
      message = message + assignmentId;
    } catch (IllegalArgumentException iae) {
      message = iae.getMessage();
    }
    // Save the Call
    resp.setContentType("text/plain");
    resp.getWriter().println(message);
    resp.flushBuffer();
  }
}
