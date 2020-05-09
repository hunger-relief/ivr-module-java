package com.dothat.ivr.notif.extractor;

import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.ivr.notif.data.IVRProvider;
import org.json.JSONObject;

/**
 * Interface for classes that extract data for an IVR source.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface IVRDataExtractor {
  IVRProvider getProvider();
  
  IVRCall extractCallData(String uri, JSONObject json);
  
  interface Factory {
    IVRDataExtractor create();
  }
}
