package com.dothat.relief.request.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.store.LocationEntity;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.*;
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
  
  private RequestStatus requestStatus;
  private ClaimStatus claimStatus;
  private VerificationStatus verificationStatus;
  
  @Load
  private Ref<LocationEntity> location;
  
  @Index
  private String source;
  @Index
  private String sourceId;
  @Index
  private SourceType sourceType;
  
  private String providerCode;
  // TODO(abhideep): Add Provider Ref here and an Index on it.
  
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

    requestStatus = data.getRequestStatus();
    claimStatus = data.getClaimStatus();
    verificationStatus = data.getVerificationStatus();
  
    if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      location = Ref.create(Key.create(LocationEntity.class, data.getLocation().getLocationId()));
    }

    source = data.getSource();
    sourceId = data.getSourceId();
    sourceType = data.getSourceType();
    
    if (data.getProvider() != null) {
      providerCode = data.getProvider().getProviderCode();
    }
  
    if (data.getCreationTimestamp() != null) {
      creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    }
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  }
  
  ReliefRequest getData() {
    ReliefRequest data = new ReliefRequest();
    data.setRequestId(requestId);
    if (requesterUUID != null) {
      ObfuscatedID id = new ObfuscatedID();
      id.setIdentifier(requesterUUID);
      data.setRequesterID(id);
    }
    
    data.setRequestType(requestType);
    data.setRequestTimestamp(JodaUtils.toDateAndTime(requestTimestamp));

    data.setRequestStatus(requestStatus);
    data.setClaimStatus(claimStatus);
    data.setVerificationStatus(verificationStatus);

    if (location != null) {
      data.setLocation(location.get().getData());
    }
    
    data.setSource(source);
    data.setSourceId(sourceId);
    data.setSourceType(sourceType);
    
    if (!Strings.isNullOrEmpty(providerCode)) {
      ReliefProvider provider = new ReliefProvider();
      provider.setProviderCode(providerCode);
      data.setProvider(provider);
    }

    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
  
    return data;
  }
}
