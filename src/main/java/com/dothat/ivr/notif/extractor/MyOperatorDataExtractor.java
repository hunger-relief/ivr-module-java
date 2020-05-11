package com.dothat.ivr.notif.extractor;

import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.ivr.notif.data.IVRProvider;
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
  public IVRCall extractCall(String uri, JSONObject json) {
    IVRCall data = new IVRCall();
    data.setProvider(getProvider());
    data.setNotificationUri(uri);
    return data;
  }
  
  @Override
  public IVRCallNode extractCallNode(String uri, JSONObject json) {
    return null;
  }
  
  static class Factory implements IVRDataExtractor.Factory {
    @Override
    public IVRDataExtractor create() {
      return new MyOperatorDataExtractor();
    }
  }
}
