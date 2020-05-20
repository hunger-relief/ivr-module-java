package com.dothat.sync.destination.servlet;

import com.dothat.common.request.RequestDataExtractor;
import com.dothat.sync.destination.DestinationService;
import com.dothat.sync.destination.data.Destination;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to register or update a Location.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RegisterDestinationServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(RegisterDestinationServlet.class);
  
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
    logger.info("Destination registration request received on URI " + uri + " : " + json.toString());
    String message = "Destination registered with Id ";
    try {
      Destination data = new DestinationExtractor().extract(json);
      Long destinationId = new DestinationService().register(data);
      message = message + destinationId;
    } catch (IllegalArgumentException iae) {
      message = iae.getMessage();
    }
    // Save the Call
    resp.setContentType("text/plain");
    resp.getWriter().println(message);
    resp.flushBuffer();
  }
}
