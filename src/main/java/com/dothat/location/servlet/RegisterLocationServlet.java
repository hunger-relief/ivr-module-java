package com.dothat.location.servlet;

import com.dothat.common.request.ContentType;
import com.dothat.common.request.RequestParameterExtractor;
import com.dothat.location.LocationRegistrationService;
import com.dothat.location.data.Location;
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
 * Servlet to register or update a Location.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RegisterLocationServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(RegisterLocationServlet.class);
  
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
    Location data = new LocationExtractor().extract(json);
    Long locationId = new LocationRegistrationService().register(data);
    // Save the Call
    resp.setContentType("text/plain");
    resp.getWriter().println("Location registered");
    resp.flushBuffer();
  }
}
