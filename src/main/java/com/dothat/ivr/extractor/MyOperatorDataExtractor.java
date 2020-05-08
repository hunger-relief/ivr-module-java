package com.dothat.ivr.extractor;

import com.dothat.ivr.data.IVRCall;
import com.dothat.ivr.data.IVRProvider;
import org.json.JSONObject;

/**
 * Extracts data for a Call on My Operator
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class MyOperatorDataExtractor implements IVRDataExtractor {
  @Override
  public IVRProvider getProvider() {
    return IVRProvider.MY_OPERATOR;
  }
  
  @Override
  public IVRCall extractCallData(String uri, JSONObject json) {
    IVRCall data = new IVRCall();
    data.setProvider(getProvider());
    data.setNotificationUri(uri);
    return data;
  }

  static class Factory implements IVRDataExtractor.Factory {
    @Override
    public IVRDataExtractor create() {
      return new MyOperatorDataExtractor();
    }
  }
}
