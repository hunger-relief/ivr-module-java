package com.dothat.relief.provider.store;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.relief.provider.data.ReliefProvider;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

/**
 * Entity object to store Location information using Objectify.
 *
 * @author abhideep@ (Abhideep Singh)
 */
@Entity
public class ProviderEntity {

  @Id
  private Long providerId;
  
  private String providerCode;
  @Index
  String providerCodeIndex;

  private DateTime creationTimestamp;
  private DateTime modificationTimestamp;

  private ProviderEntity() {
    // Empty Constructor for use by Objectify only
  }
  
  public ProviderEntity(ReliefProvider data) {
    this();
    providerId = data.getProviderId();
  
    providerCode = data.getProviderCode();
    providerCodeIndex = toIndexValue(data.getProviderCode());

    creationTimestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    modificationTimestamp = JodaUtils.toDateTime(data.getModificationTimestamp());
  }
  
  public ReliefProvider getData() {
    ReliefProvider data = new ReliefProvider();
    data.setProviderId(providerId);
    data.setProviderCode(providerCode);

    data.setCreationTimestamp(JodaUtils.toDateAndTime(creationTimestamp));
    data.setModificationTimestamp(JodaUtils.toDateAndTime(modificationTimestamp));
    return data;
  }
  
  private String toIndexValue(String value) {
    return value.toUpperCase();
  }
}
