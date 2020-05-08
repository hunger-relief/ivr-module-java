package com.dothat.ivr.extractor;

import com.dothat.ivr.data.IVRCall;
import com.dothat.ivr.data.IVRProvider;
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
