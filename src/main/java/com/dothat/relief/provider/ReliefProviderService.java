package com.dothat.relief.provider;

import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestType;

/**
 * Service Layer that provides information about the Relief Provider
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefProviderService {
  
  public ReliefProvider assignProvider(ObfuscatedID requesterId, RequestType requestType, Location location) {
    // TODO(abhideep): Add Provider lookup logic here
    ReliefProvider data = new ReliefProvider();
    data.setProviderCode("GNEM");
    return data;
  }
}
