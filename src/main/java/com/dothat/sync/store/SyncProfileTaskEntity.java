package com.dothat.sync.store;

import com.dothat.profile.store.ProfileAttributeEntity;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.provider.store.ProviderEntity;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.store.ReliefRequestEntity;
import com.dothat.sync.data.SyncProcessType;
import com.dothat.sync.data.SyncProfileTask;
import com.dothat.sync.data.SyncRequestTask;
import com.dothat.sync.destination.store.DestinationEntity;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

/**
 * Entity that stores Sync Tasks for Requests using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class SyncProfileTaskEntity {
  @Id
  private Long taskId;
  
  private SyncProcessType processType;
  @Index
  private String processTaskName;
  
  private String providerCode;
  @Load
  private Ref<ProviderEntity> provider;
  private RequestType requestType;
  @Load
  private Ref<DestinationEntity> destination;
  
  @Parent @Load
  private Ref<ProfileAttributeEntity> attribute;
  
  private SyncProfileTaskEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  SyncProfileTaskEntity(SyncProfileTask data) {
    this();
    taskId = data.getTaskId();
  
    processType = data.getProcessType();
    processTaskName = data.getProcessTaskName();
    
    providerCode = data.getProvider().getProviderCode();
    provider = Ref.create(Key.create(ProviderEntity.class, data.getProvider().getProviderId()));
    requestType = data.getRequestType();

    destination = Ref.create(Key.create(DestinationEntity.class, data.getDestination().getDestinationId()));
  
    attribute = Ref.create(Key.create(ProfileAttributeEntity.class,
        data.getProfileAttribute().getAttributeId()));
  }
  
  SyncProfileTask getData() {
    SyncProfileTask data = new SyncProfileTask();

    data.setTaskId(taskId);
    data.setProcessType(processType);
    data.setProcessTaskName(processTaskName);

    if (provider != null) {
      data.setProvider(provider.get().getData());
    } else if (providerCode != null) {
      ReliefProvider providerData = new ReliefProvider();
      providerData.setProviderCode(providerCode);
      data.setProvider(providerData);
    }
    data.setRequestType(requestType);
    data.setDestination(destination.get().getData());
    data.setProfileAttribute(attribute.get().getData());
    
    return data;
  }
}
