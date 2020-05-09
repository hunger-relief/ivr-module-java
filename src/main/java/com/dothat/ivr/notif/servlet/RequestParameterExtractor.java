package com.dothat.ivr.notif.servlet;

import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Extracts the request parameters as a Map of List Values
 *
 * @author abhideep@ (Abhideep Singh)
 */
class RequestParameterExtractor {

  private static final Logger logger = LoggerFactory.getLogger(RequestParameterExtractor.class);
  
  static Map<String, Object> prepareForJson(Map<String, List<String>> parameters) {
    Map<String, Object> map = new HashMap<>();
    for (String key : parameters.keySet()) {
      List<String> values = parameters.get(key);
      if (values.size() == 1) {
        map.put(key, values.get(0));
      } else if (values.size() > 1) {
        map.put(key, values);
      }
    }
    return map;
  }
  
  static Map<String, List<String>> extractFromGetRequest(String queryString)
      throws UnsupportedEncodingException {
    Map<String, List<String>> parameters = new HashMap<>();
    if (queryString != null) {
      String[] queryParameters = queryString.split("&");
      for (String queryParameter : queryParameters) {
        String nameValuePair = URLDecoder.decode(queryParameter, "UTF-8");
        String value = null;
        String key = nameValuePair;
        int separatorIndex = queryParameter.indexOf("=");
        if (separatorIndex > 0) {
          key = nameValuePair.substring(0, separatorIndex);
          if (nameValuePair.length() > separatorIndex + 1) {
            value = nameValuePair.substring(separatorIndex + 1);
          }
        }
        // key = key.toUpperCase();
        if (!parameters.containsKey(key)) {
          parameters.put(key, new LinkedList<>());
        }
        parameters.get(key).add(value);
      }
    }
    return parameters;
  }

  static Map<String, List<String>> extractFromPostRequest(HttpServletRequest req) {
    Map<String, List<String>> parameters = new HashMap<>();
    Map<String, String[]> parameterMap = req.getParameterMap();
    if (parameterMap != null) {
      for (Object mapKey : parameterMap.keySet()) {
        String key = (String) mapKey;
        // key = key.toUpperCase();
        if (!parameters.containsKey(key)) {
          parameters.put(key, new LinkedList<>());
        }
        String[] values = req.getParameterValues(key);
        if (values != null) {
          parameters.get(key).addAll(Arrays.asList(values));
        }
      }
    }
    return parameters;
  }
  
  public static JSONObject extractFromJsonRequest(HttpServletRequest req) {
    StringBuilder builder = new StringBuilder();
    String line;
    try {
      BufferedReader reader = req.getReader();
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
    } catch (IOException ioe) {
      logger.error("Error reading JSON content for " + req.getRequestURI(), ioe);
      throw new RuntimeException("Error reading JSON content for " + req.getRequestURI());
    }

    String content = builder.toString();
    try {
      return HTTP.toJSONObject(content);
    } catch (JSONException e) {
      logger.error("Error parsing String into JSON Object for " + req.getRequestURI(), e);
      throw e;
    }
  }
}
