package com.dothat.common.request;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Extracts the Request as a JSON object.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestDataExtractor {

  public JSONObject extractGet(HttpServletRequest req) throws UnsupportedEncodingException {
    String queryString = req.getQueryString();
    Map<String, List<String>> parameters = RequestParameterExtractor.extractFromGetRequest(queryString);
    return new JSONObject(RequestParameterExtractor.prepareForJson(parameters));
  }
  
  public JSONObject extractPost(HttpServletRequest req) throws UnsupportedEncodingException {
    String contentType = req.getContentType();
    if (ContentType.TEXT_PLAIN.equals(contentType)) {
      Map<String, List<String>> parameters = RequestParameterExtractor.extractFromPostRequest(req);
      return new JSONObject(RequestParameterExtractor.prepareForJson(parameters));
    } else if (ContentType.JSON.equals(contentType)) {
      return RequestParameterExtractor.extractFromJsonRequest(req);
    }
    throw new UnsupportedEncodingException("Unsupported Content Type " + contentType);
  }
}
