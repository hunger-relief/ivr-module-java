package com.dothat.relief.provider;

import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ProviderConfig;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestType;
import com.google.common.base.Strings;

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
  
  public static final String DEFAULT = GNEM;
  public static final String DEFAULT_SHEET = GNEM_SHEET;
  
  public ReliefProvider assignProvider(ObfuscatedID requesterId, RequestType requestType, Location location) {
    // TODO(abhideep): Add Provider lookup logic here
    ReliefProvider data = new ReliefProvider();
    data.setProviderCode(DEFAULT);
    return data;
  }
  
  public ProviderConfig getProviderConfig(ReliefProvider provider) {
    if (provider == null || Strings.isNullOrEmpty(provider.getProviderCode())) {
      return null;
    }
    if (DEFAULT.equals(provider.getProviderCode())) {
      ProviderConfig data = new ProviderConfig();
      data.setProviderCode(DEFAULT);
      data.setGoogleSheetId(DEFAULT_SHEET);
      return data;
    }
    return null;
  }
}
