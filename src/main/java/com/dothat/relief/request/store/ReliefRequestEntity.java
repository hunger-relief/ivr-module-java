package com.dothat.relief.request.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.store.LocationEntity;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.provider.store.ProviderEntity;
import com.dothat.relief.request.data.ClaimStatus;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestStatus;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import com.dothat.relief.request.data.VerificationStatus;
import com.google.common.base.Strings;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import org.joda.time.DateTime;

/**
 * Entity that stores Relief Request using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class ReliefRequestEntity {
  @Id
  private Long requestId;
  
  @Index
  private String requesterUUID;
  @Index
  private RequestType requestType;
  @Index
  private DateTime requestTimestamp;
  private String requestReceiver;

  private RequestStatus requestStatus;
  private ClaimStatus claimStatus;
  private VerificationStatus verificationStatus;

  @Load
  private Ref<LocationEntity> location;

  @Index
  private SourceType sourceType;
  @Index
  private String source;
  @Index
  private String sourceRootId;
  @Index
  private String sourceId;

  @Index
  private Long providerId;
  private String providerCode;
  @Load
  private Ref<ProviderEntity> provider;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;
  
  private ReliefRequestEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  ReliefRequestEntity(ReliefRequest data) {
    this();
    requestId = data.getRequestId();
    if (data.getRequesterID() != null) {
      requesterUUID = data.getRequesterID().getIdentifier();
    }
    requestType = data.getRequestType();
    requestTimestamp = JodaUtils.toDateTime(data.getRequestTimestamp());
    requestReceiver = data.getRequestReceiver();

    requestStatus = data.getRequestStatus();
    claimStatus = data.getClaimStatus();
    verificationStatus = data.getVerificationStatus();

    if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      location = Ref.create(Key.create(LocationEntity.class, data.getLocation().getLocationId()));
    }

    sourceType = data.getSourceType();
    source = data.getSource();
    sourceRootId = data.getSourceRootId();
    sourceId = data.getSourceId();

    if (data.getProvider() != null && data.getProvider().getProviderId() != null) {
      providerId = data.getProvider().getProviderId();
      providerCode = data.getProvider().getProviderCode();
      provider = Ref.create(Key.create(ProviderEntity.class, data.getProvider().getProviderId()));
    }
  
    if (data.getCreationTimestamp() != null) {
      creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    }
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  }
  
  public ReliefRequest getData() {
    ReliefRequest data = new ReliefRequest();
    data.setRequestId(requestId);
    if (requesterUUID != null) {
      ObfuscatedID id = new ObfuscatedID();
      id.setIdentifier(requesterUUID);
      data.setRequesterID(id);
    }
    
    data.setRequestType(requestType);
    data.setRequestTimestamp(JodaUtils.toDateAndTime(requestTimestamp));
    data.setRequestReceiver(requestReceiver);

    data.setRequestStatus(requestStatus);
    data.setClaimStatus(claimStatus);
    data.setVerificationStatus(verificationStatus);

    if (location != null) {
      data.setLocation(location.get().getData());
    }

    data.setSourceType(sourceType);
    data.setSource(source);
    data.setSourceRootId(sourceRootId);
    data.setSourceId(sourceId);

    if (providerId != null && provider != null) {
      data.setProvider(provider.get().getData());
    } else if (!Strings.isNullOrEmpty(providerCode)) {
      // This is for Legacy Requests that didn't have ref to the Provider
      ReliefProvider provider = new ReliefProvider();
      provider.setProviderCode(providerCode);
      data.setProvider(provider);
    }

    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
  
    return data;
  }
}
