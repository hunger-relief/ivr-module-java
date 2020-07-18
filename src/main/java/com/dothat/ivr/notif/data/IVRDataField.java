package com.dothat.ivr.notif.data;

import com.dothat.common.field.Field;

public enum IVRDataField implements Field {
  CALL_ID,
  CALL_NODE_ID,
  
  CALLER_NUMBER,
  COUNTRY,
  CIRCLE,
  LOCATION_HINT,
  
  IVR_NUMBER, // The number for the Bridge or IVR
  DIALED_NUMBER, // The number that was dialed by the caller
  RECEIVER_NUMBER, // The number of the agent who received the call

  URGENCY,
  KEY_PRESS,
  
  CALL_TIMESTAMP,
  CALL_DURATION,
  CALL_START_TIMESTAMP,
  CALL_END_TIMESTAMP,
  ;
  
  @Override
  public String getFieldName() {
    return name();
  }
}
