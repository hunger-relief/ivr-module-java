/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.dothat.ivr.webhook;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Servlet to receive notifications about nodes visited by the caller in the IVR Call
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class NodeVisitLogger extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    String queryString = req.getQueryString();
    Map<String, List<String>> parameters = RequestParameterExtractor.extractFromGetRequest(queryString);
    processRequest(uri, parameters, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    Map<String, List<String>> parameters = RequestParameterExtractor.extractFromPostRequest(req);
    processRequest(uri, parameters, resp);
  }

  private void processRequest(String uri, Map<String, List<String>> parameters, HttpServletResponse resp)
      throws IOException {
    resp.setContentType("text/plain");
    resp.getWriter().println("Notification Received on uri " + uri);
    resp.flushBuffer();
  }
}
