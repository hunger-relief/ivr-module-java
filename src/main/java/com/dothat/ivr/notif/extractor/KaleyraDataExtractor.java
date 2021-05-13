package com.dothat.ivr.notif.extractor;

import com.dothat.common.field.Field;
import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.ivr.notif.data.IVRDataField;
import com.dothat.ivr.notif.data.IVRProvider;
import com.dothat.location.data.Country;
import com.dothat.location.data.State;
import com.dothat.location.impl.IndiaState;
import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.validate.phone.CountryCode;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Extracts data for a Call on Kaleyra.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class KaleyraDataExtractor extends GenericIVRDataExtractor {
  

  public KaleyraDataExtractor() {
    super(IVRProvider.KALEYRA);
    registerField(IVRDataField.CALLER_NUMBER, "caller");
    registerField(IVRDataField.DIALED_NUMBER, "called");
    registerField(IVRDataField.IVR_NUMBER, "number");
    registerField(IVRDataField.RECEIVER_NUMBER, "dialed");
    registerField(IVRDataField.RELAY_MODE, "relay");
    registerField(IVRDataField.CALL_ID, "id");
    registerField(IVRDataField.COUNTRY, "isocode");
    registerField(IVRDataField.CIRCLE, "circle");
    registerField(IVRDataField.CALL_TIMESTAMP, "calltime");
    registerField(IVRDataField.CALL_DURATION, "duration");
    registerField(IVRDataField.CALL_NODE_ID, "nodeid");
    registerField(IVRDataField.KEY_PRESS, "keypress");

    registerCountryCode(CountryCode.INDIA.getCode(), Country.INDIA);

    registerStateCode("DELHI", IndiaState.DELHI);
    registerStateCode("HARYANA", IndiaState.HARYANA);
    registerStateCode("TAMIL NADU", IndiaState.TAMIL_NADU);
  }
  

  @Override
  public IVRCall extractCall(String uri, JSONObject json) {
    IVRCall data = super.extractCall(uri, json);
    // Set Call Timestamp
    DateTime now = DateTime.now();
    LocalDate today = LocalDate.now();
    // TODO(abhideep): Get this from the Web Hook / API data itself.
    data.setCallDate(JodaUtils.toSimpleDate(today));
    data.setCallTimestamp(JodaUtils.toDateAndTime(now));

    data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    return data;
  }
  
  @Override
  public IVRCallNode extractCallNode(String uri, JSONObject json) {
    IVRCallNode data = super.extractCallNode(uri, json);

    // Set Call Timestamp
    DateTime now = DateTime.now();
    // TODO(abhideep): Get this from the Web Hook / API data itself.
    data.setTimestamp(JodaUtils.toDateAndTime(now));
  
    data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));

    return data;
  }
  
  public static class Factory implements IVRDataExtractor.Factory {
  
    @Override
    public IVRDataExtractor create() {
      return new KaleyraDataExtractor();
    }
  }
}
