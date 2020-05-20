package com.dothat.relief.provider;

import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.data.Location;
import com.dothat.relief.provider.data.ProviderAssignment;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestSource;
import com.dothat.relief.provider.store.ProviderStore;
import com.dothat.relief.request.data.RequestType;

import java.util.List;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefProviderAssigner {
  
  private final ProviderStore store = new ProviderStore();
  
  public ReliefProvider assignProvider(ObfuscatedID requesterId, RequestSource source,
                                       RequestType requestType, Location location) {
    if (source != null) {
      // First lookup Association by Request Source, Location, and Request Type.
      List<ProviderAssignment> providerList = store.findAllAssociations(source, requestType, location);

      if (providerList == null || providerList.isEmpty()) {
        // Then lookup Association by just the Request Source, to see if a number is reserved for a Provider.
        providerList = store.findAllAssociations(source, null, null);
      }

      if (providerList != null && providerList.size() == 1) {
        return providerList.get(0).getProvider();
      }
    }
    
    // Otherwise lookup all providers for Request Type and Location and assign it to one.
    // TODO(abhideep): Add Logic for better distribution of requests.
    
    List<ProviderAssignment> providerList = store.findAllAssociations(null, requestType, location);
    if (providerList != null && !providerList.isEmpty()) {
      // TODO(abhideep): Check if any of the Providers have processed this person before.
      // TODO(abhideep): Improve algorithm for assignment.
      int assignIndex = requesterId.hashCode() % providerList.size();
      ProviderAssignment provider = providerList.get(assignIndex);
      if (provider != null && provider.getProvider() != null) {
        return provider.getProvider();
      }
    }
    return null;
  }
}
