package com.dothat.relief.provider.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.location.store.LocationEntity;
import com.dothat.relief.provider.data.ProviderAssignment;
import com.dothat.relief.request.data.RequestSource;
import com.dothat.relief.request.data.RequestType;
import com.dothat.relief.request.data.SourceType;
import com.google.common.base.Strings;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import org.joda.time.DateTime;

/**
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class ProviderAssignmentEntity {
  @Id
  private Long associationId;

  @Index
  private String dialedNumber;
  @Index
  private SourceType sourceType;
  @Index
  private String source;

  @Index
  private RequestType requestType;
  @Index @Load
  private Ref<LocationEntity> location;
  
  @Index @Load
  private Ref<ProviderEntity> provider;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;

  private ProviderAssignmentEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public ProviderAssignmentEntity(ProviderAssignment data) {
    this();
    associationId = data.getAssignmentId();
  
    if (data.getSource() != null) {
      dialedNumber = data.getSource().getDialedNumber();
      sourceType = data.getSource().getSourceType();
      source = data.getSource().getSource();
    }

    requestType = data.getRequestType();

    if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      location = Ref.create(Key.create(LocationEntity.class, data.getLocation().getLocationId()));
    }
  
    if (data.getProvider() != null && data.getProvider().getProviderId() != null) {
      provider = Ref.create(Key.create(ProviderEntity.class, data.getProvider().getProviderId()));
    }
  
    creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  }
  
  public ProviderAssignment getData() {
    ProviderAssignment data = new ProviderAssignment();

    data.setAssignmentId(associationId);

    if (!Strings.isNullOrEmpty(dialedNumber) && sourceType != null
        && !Strings.isNullOrEmpty(source)) {
      RequestSource sourceData = new RequestSource();
      sourceData.setDialedNumber(dialedNumber);
      sourceData.setSource(source);
      sourceData.setSourceType(sourceType);
      data.setSource(sourceData);
    }
    
    data.setRequestType(requestType);
    
    if (location != null) {
      data.setLocation(location.get().getData());
    }
    
    if (provider != null) {
      data.setProvider(provider.get().getData());
    }
  
    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
    return data;
  }
}
