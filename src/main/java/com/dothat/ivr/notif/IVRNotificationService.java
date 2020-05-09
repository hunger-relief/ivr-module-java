package com.dothat.ivr.notif;

import com.dothat.ivr.notif.data.IVRCall;
import com.dothat.ivr.notif.store.IVRCallStore;

/**
 * Service layer for IVR based Services.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRNotificationService {
  private final IVRCallStore store = new IVRCallStore();
  
  public Long save(IVRCall data) {
    return store.store(data);
  }
}
