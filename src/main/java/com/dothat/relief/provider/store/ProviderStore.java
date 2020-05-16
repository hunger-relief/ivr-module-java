package com.dothat.relief.provider.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.relief.provider.data.ReliefProvider;
import com.googlecode.objectify.Key;

import java.util.List;

/**
 * Stores the data for a Relief Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProviderStore {
  static {
    // Register all Entities used by the Store
    PersistenceService.factory().register(ProviderEntity.class);
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
  }
  
  
  public Long store(ReliefProvider data) {
    return PersistenceService.service().transact(() -> {
    
      // Save the data
      ProviderEntity location = new ProviderEntity(data);
      Key<ProviderEntity> key = PersistenceService.service().save().entity(location).now();
    
      // Extract the Provider Id
      Long providerId = key.getId();
    
      // Set the Location Id on the Location Data for new Locations
      data.setProviderId(providerId);
      return providerId;
    });
  }
  
  public ReliefProvider find(String code) {
    List<ProviderEntity> provider = PersistenceService.service().load()
        .type(ProviderEntity.class)
        .filter("providerCodeIndex", code.toUpperCase())
        .list();
  
    if (provider == null || provider.isEmpty()) {
      return null;
    }
    if (provider.size() != 1) {
      throw new IllegalStateException("Multiple Providers registered with code " + code);
    }
    return provider.get(0).getData();
  }
}
