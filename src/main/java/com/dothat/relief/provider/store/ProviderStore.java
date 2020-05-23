package com.dothat.relief.provider.store;

import com.dothat.common.objectify.PersistenceService;
import com.dothat.location.data.Location;
import com.dothat.location.store.LocationStore;
import com.dothat.relief.provider.data.AssignInstruction;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.RequestSource;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
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
    PersistenceService.factory().register(AssignInstructionEntity.class);
  
    // Initialize the Dependencies
    LocationStore.init();
    ProviderStore.init();
  }
  
  public static void init() {
    // Empty initialization used to trigger the static block
  }
  
  
  public Long store(ReliefProvider data) {
    return PersistenceService.service().transact(() -> {
    
      // Save the data
      ProviderEntity provider = new ProviderEntity(data);
      Key<ProviderEntity> key = PersistenceService.service().save().entity(provider).now();
    
      // Extract the Provider Id
      Long providerId = key.getId();
    
      // Set the Provider Id on new Providers.
      data.setProviderId(providerId);
      return providerId;
    });
  }
  
  public Long storeInstruction(AssignInstruction data) {
    return PersistenceService.service().transact(() -> {
      
      // Save the data
      AssignInstructionEntity association = new AssignInstructionEntity(data);
      Key<AssignInstructionEntity> key = PersistenceService.service().save().entity(association).now();
      
      // Extract the Association Id
      Long associationId = key.getId();
      
      // Set the Association Id on the Association Data for new Associations
      data.setInstructionId(associationId);
      return associationId;
    });
  }
  
  public ReliefProvider find(String code) {
    List<ProviderEntity> providerList = PersistenceService.service().load()
        .type(ProviderEntity.class)
        .filter("providerCodeIndex", code.toUpperCase())
        .list();
  
    if (providerList == null || providerList.isEmpty()) {
      return null;
    }
    if (providerList.size() != 1) {
      throw new IllegalStateException("Multiple Providers registered with code " + code);
    }
    return providerList.get(0).getData();
  }
  
  public ReliefProvider find(Long providerId) {
    ProviderEntity provider = PersistenceService.service().load()
        .type(ProviderEntity.class)
        .id(providerId)
        .now();
    if (provider == null) {
      return null;
    }
    return provider.getData();
  }
  
  public AssignInstruction findInstruction(AssignInstruction data) {
    SourceType sourceType = null;
    String source = null;
    String dialedNumber = null;
    Long locationId = null;
  
    if (data.getSource() != null) {
      sourceType = data.getSource().getSourceType();
      source = data.getSource().getSource();
      dialedNumber = data.getSource().getDialedNumber();
    }
  
    if (data.getLocation() != null) {
      locationId = data.getLocation().getLocationId();
    }

    Query<AssignInstructionEntity> query = PersistenceService.service().load()
        .type(AssignInstructionEntity.class)
        .filter("providerId", data.getProvider().getProviderId())
        .filter("requestType", data.getRequestType())
        .filter("dialedNumber", dialedNumber)
        .filter("sourceType", sourceType)
        .filter("source", source)
        .filter("locationId", locationId);
  
    List<AssignInstructionEntity> providerList = query.list();
  
    if (providerList == null || providerList.isEmpty()) {
      return null;
    }
    
    if (providerList.size() == 1) {
      return providerList.get(0).getData();
    }
    
    throw new IllegalStateException("More than one Association found for "
        + data.getProvider().getProviderCode() + " for Source " + sourceType + " " + source
        + " number " + dialedNumber + " Request Type " + data.getRequestType() + " Location "
        + locationId);
  }
    
    public List<AssignInstruction> findAllInstructions(
      RequestSource source, RequestType requestType, Location location) {
    Query<AssignInstructionEntity> query = PersistenceService.service().load()
        .type(AssignInstructionEntity.class)
        .filter("requestType", requestType);

    if (source != null) {
      query = query
          .filter("dialedNumber", source.getDialedNumber())
          .filter("sourceType", source.getSourceType())
          .filter("source", source.getSource());
    } else {
      query = query
          .filter("dialedNumber", null)
          .filter("sourceType", null)
          .filter("source", null);
    }
    
    if (location == null) {
      query = query.filter("locationId", null);
    } else {
      query = query.filter("locationId", location.getLocationId());
    }

    List<AssignInstructionEntity> providerList = query.list();
  
    if (providerList != null && !providerList.isEmpty()) {
      List<AssignInstruction> dataList = new ArrayList<>();
      for (AssignInstructionEntity provider : providerList) {
        dataList.add(provider.getData());
      }
      return dataList;
    }
    return null;
  }
}
