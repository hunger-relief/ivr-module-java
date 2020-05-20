package com.dothat.sync.destination.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.location.store.LocationEntity;
import com.dothat.relief.provider.store.ProviderEntity;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.destination.data.DestinationType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import org.joda.time.DateTime;

/**
 * Entity to store destination information using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class DestinationEntity {
  @Id
  private Long destinationId;
  
  @Index @Load
  private Ref<ProviderEntity> provider;
  @Index
  private RequestType requestType;
  @Index @Load
  private Ref<LocationEntity> location;
  
  private DestinationType destinationType;
  private String googleSheetId;
  
  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;

  private DestinationEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  DestinationEntity(Destination data) {
    this();
    destinationId = data.getDestinationId();

    provider = Ref.create(Key.create(ProviderEntity.class, data.getProvider().getProviderId()));
    requestType = data.getRequestType();
    if (data.getLocation() != null && data.getLocation().getLocationId() != null) {
      location = Ref.create(Key.create(LocationEntity.class, data.getLocation().getLocationId()));
    }
    
    destinationType = data.getDestinationType();
    googleSheetId = data.getGoogleSheetId();
  
    creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  }
  
  Destination getData() {
    Destination data = new Destination();
    data.setDestinationId(destinationId);
    
    data.setProvider(provider.get().getData());
    data.setRequestType(requestType);
    if (location != null) {
      data.setLocation(location.get().getData());
    }
    data.setRequestType(requestType);
    
    data.setDestinationType(destinationType);
    data.setGoogleSheetId(googleSheetId);
  
    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
    return data;
  }
}
