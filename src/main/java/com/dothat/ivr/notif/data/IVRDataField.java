package com.dothat.ivr.notif.data;

import com.dothat.common.field.Field;

public enum IVRDataField implements Field {
  CALL_ID,
  CALL_NODE_ID,
  
  CALLER_NUMBER,
  COUNTRY,
  CIRCLE,
  LOCATION_HINT,
  
  IVR_NUMBER,
  DIALED_NUMBER,

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
