package com.dothat.ivr;

import com.dothat.ivr.data.IVRCall;
import com.dothat.ivr.store.IVRCallStore;

/**
 * Service layer for IVR based Services.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRLogService {
  private final IVRCallStore store = new IVRCallStore();
  
  public Long save(IVRCall data) {
    return store.store(data);
  }
}
