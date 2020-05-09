package com.dothat.ivr.notif.servlet;

import com.dothat.ivr.notif.data.IVRProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A Registry for URLs where Call Log Notifications are sent.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class CallLogUrlRegistry {
  public static final Logger logger = LoggerFactory.getLogger(CallLogUrlRegistry.class);
  
  private static final CallLogUrlRegistry INSTANCE = new CallLogUrlRegistry();

  private final Map<String, IVRProvider> uriMap = new HashMap<>();
  
  private CallLogUrlRegistry() {
    uriMap.put("/ivr/call/callerdesk/", IVRProvider.CALLER_DESK);
    uriMap.put("/ivr/call/myoperator/log/", IVRProvider.MY_OPERATOR);
    uriMap.put("/ivr/call/kaleyra/log/", IVRProvider.KALEYRA);
    uriMap.put("/ivr/call/knowlarity/", IVRProvider.KNOWLARITY);
  }
  
  public static CallLogUrlRegistry getInstance() {
    return INSTANCE;
  }
  
  public IVRProvider getProvider(String uri) {
    if (uri != null && !uri.endsWith("/")) {
      uri = uri + "/";
    }
    IVRProvider provider = uriMap.get(uri);
    if (provider == null) {
      logger.warn("No provider registered for URI " + uri);
    } else {
      logger.info("Request URI " + uri + " maps to Provider " + provider);
    }
    return provider;
  }
}
