package com.dothat.location.servlet;

import com.dothat.common.request.RequestDataExtractor;
import com.dothat.location.LocationService;
import com.dothat.location.data.Location;
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
public class RegisterLocationServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(RegisterLocationServlet.class);
  
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
    String message = "Location registered with Id ";
    try {
      Location data = new LocationExtractor().extract(json);
      Long locationId = new LocationService().register(data);
      message = message + String.valueOf(locationId);
    } catch (IllegalArgumentException iae) {
      message = iae.getMessage();
    }
    // Save the Call
    resp.setContentType("text/plain");
    resp.getWriter().println(message);
    resp.flushBuffer();
  }
}
