package com.dothat.relief.provider.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.location.store.LocationEntity;
import com.dothat.relief.provider.data.AssignInstruction;
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
 * Entity object to store Provider Assignment Instructions using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class AssignInstructionEntity {
  @Id
  private Long instructionId;

  @Index
  private String dialedNumber;
  @Index
  private SourceType sourceType;
  @Index
  private String source;

  @Index
  private RequestType requestType;
  @Index
  private Long locationId;
  @Load
  private Ref<LocationEntity> location;
  
  
  @Index
  private Long providerId;
  @Load
  private Ref<ProviderEntity> provider;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;

  private AssignInstructionEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public AssignInstructionEntity(AssignInstruction data) {
    this();
    instructionId = data.getInstructionId();
  
    if (data.getSource() != null) {
      dialedNumber = data.getSource().getDialedNumber();
      sourceType = data.getSource().getSourceType();
      source = data.getSource().getSource();
    }

    requestType = data.getRequestType();

    if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      locationId = data.getLocation().getLocationId();
      location = Ref.create(Key.create(LocationEntity.class, data.getLocation().getLocationId()));
    }
  
    if (data.getProvider() != null && data.getProvider().getProviderId() != null) {
      providerId = data.getProvider().getProviderId();
      provider = Ref.create(Key.create(ProviderEntity.class, data.getProvider().getProviderId()));
    }
  
    creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  }
  
  public AssignInstruction getData() {
    AssignInstruction data = new AssignInstruction();

    data.setInstructionId(instructionId);

    if (!Strings.isNullOrEmpty(dialedNumber) && sourceType != null
        && !Strings.isNullOrEmpty(source)) {
      RequestSource sourceData = new RequestSource();
      sourceData.setDialedNumber(dialedNumber);
      sourceData.setSource(source);
      sourceData.setSourceType(sourceType);
      data.setSource(sourceData);
    }
    
    data.setRequestType(requestType);
    
    if (locationId != null && location != null) {
      data.setLocation(location.get().getData());
    }
    
    if (providerId != null && provider != null) {
      data.setProvider(provider.get().getData());
    }
  
    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
    return data;
  }
}
