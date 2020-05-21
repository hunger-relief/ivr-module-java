package com.dothat.relief.provider;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.location.LocationDisplayUtils;
import com.dothat.relief.provider.data.ProviderAssignment;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.provider.store.ProviderStore;
import com.dothat.relief.request.data.RequestSource;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service Layer that provides information about the Relief Provider
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ReliefProviderService {
  private static final Logger logger = LoggerFactory.getLogger(ReliefProviderService.class);

  private static final String GNEM = "GNEM";
  private static final String GNEM_SHEET = "18DLnhZJ2r7fT9hxCSK2DEsQPp3X1lIvRfMFMgxpe_C0";
  private static final String DEMO = "DEMO";
  private static final String DEMO_SHEET = "1nYypj7tClNkDzmctfnpN1kKf9fiNN1236cvNrNPEeZI";
  
  public static final String DEFAULT = DEMO;
  public static final String DEFAULT_SHEET = DEMO_SHEET;
  
  private final ProviderStore store = new ProviderStore();
  
  public ReliefProvider lookupByCode(String code) {
    return store.find(code);
  }

  public ReliefProvider lookupById(Long providerId) {
    return store.find(providerId);
  }

  public ProviderAssignment lookupAssociation(ProviderAssignment data) {
    ReliefProvider provider = data.getProvider();
    if (provider == null) {
      throw new IllegalArgumentException("Must specify the Provider that you are looking up the Instruction for");
    }
    if (data.getSource() != null) {
      RequestSource source = data.getSource();
      logger.info("Find Assignment Instructions for " + data.getRequestType()
          + " from " + data.getProvider().getProviderCode() + " [ID " + data.getProvider().getProviderId() + " ]"
          + " sourced from " + source.getSourceType() + " " + source.getDialedNumber()
          + " and Location " + LocationDisplayUtils.forLog(data.getLocation())
          + " [ID " + LocationDisplayUtils.idForLog(data.getLocation()) + " ]");
    } else {
      logger.info("Find Assignment Instructions for " + data.getRequestType()
          + " from " + data.getProvider().getProviderCode() + " [ID " + data.getProvider().getProviderId() + " ]"
          + " and Location " + LocationDisplayUtils.forLog(data.getLocation())
          + " [ID " + LocationDisplayUtils.idForLog(data.getLocation()) + " ]");
    }
    return store.findAssociation(data);
  }
  
  public Long registerAssignment(ProviderAssignment data) {
    new ProviderAssignmentValidator().validate(data);
    ProviderAssignment currentData = lookupAssociation(data);
    if (currentData != null) {
      data.setAssignmentId(currentData.getAssignmentId());
      data.setCreationTimestamp(currentData.getCreationTimestamp());
    }
    ReliefProvider provider = data.getProvider();
    if (provider == null) {
      throw new IllegalArgumentException("Must specify the Provider that you are registering the Instruction for");
    }
    if (data.getSource() != null) {
      RequestSource source = data.getSource();
      logger.info("Registering Assignment Instructions for " + data.getRequestType()
          + " from " + data.getProvider().getProviderCode() + " [ID " + data.getProvider().getProviderId() + " ]"
          + " sourced from " + source.getSourceType() + " " + source.getDialedNumber()
          + " and Location " + LocationDisplayUtils.forLog(data.getLocation())
          + " [ID " + LocationDisplayUtils.idForLog(data.getLocation()) + " ]");
    } else {
      logger.info("Registering Assignment Instructions for " + data.getRequestType()
          + " from " + data.getProvider().getProviderCode() + " [ID " + data.getProvider().getProviderId() + " ]"
          + " and Location " + LocationDisplayUtils.forLog(data.getLocation())
          + " [ID " + LocationDisplayUtils.idForLog(data.getLocation()) + " ]");
    }
  
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    return store.storeAssignment(data);
  }
  
  public Long register(ReliefProvider data) {
    ReliefProvider currentData = store.find(data.getProviderCode());
    if (currentData != null) {
      data.setProviderId(currentData.getProviderId());
      data.setCreationTimestamp(currentData.getCreationTimestamp());
    }
    
    DateTime now = DateTime.now();
    if (data.getCreationTimestamp() == null) {
      data.setCreationTimestamp(JodaUtils.toDateAndTime(now));
    }
    data.setModificationTimestamp(JodaUtils.toDateAndTime(now));
    
    return store.store(data);
  }
}
