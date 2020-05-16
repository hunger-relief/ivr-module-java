package com.dothat.relief.provider;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ProviderConfig;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.provider.store.ProviderStore;
import com.dothat.relief.request.data.RequestType;
import com.google.common.base.Strings;
import org.joda.time.DateTime;

/**
 * Service Layer that provides information about the Relief Provider
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefProviderService {
  private static final String GNEM = "GNEM";
  private static final String GNEM_SHEET = "18DLnhZJ2r7fT9hxCSK2DEsQPp3X1lIvRfMFMgxpe_C0";
  private static final String DEMO = "DEMO";
  private static final String DEMO_SHEET = "1nYypj7tClNkDzmctfnpN1kKf9fiNN1236cvNrNPEeZI";
  
  public static final String DEFAULT = DEMO;
  public static final String DEFAULT_SHEET = DEMO_SHEET;
  
  private final ProviderStore store = new ProviderStore();
  
  public ReliefProvider assignProvider(ObfuscatedID requesterId, RequestType requestType, Location location) {
    // TODO(abhideep): Add Provider lookup logic here
    ReliefProvider data = new ReliefProvider();
    data.setProviderCode(DEFAULT);
    return data;
  }
  
  public ProviderConfig getProviderConfig(String providerCode) {
    if (Strings.isNullOrEmpty(providerCode)) {
      return null;
    }
    ReliefProvider provider = store.find(providerCode);
    if (provider != null) {
      return provider.getConfig();
    }
    return null;
  }
  
  public Long register(ReliefProvider data) {
    ReliefProvider currentData = store.find(data.getProviderCode());
    if (currentData != null) {
      data.setProviderId(currentData.getProviderId());
      data.setCreationTimestamp(currentData.getCreationTimestamp());
    }
    
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    
    return store.store(data);
  }
}
