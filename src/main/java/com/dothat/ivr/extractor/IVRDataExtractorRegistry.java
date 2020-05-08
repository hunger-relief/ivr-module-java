package com.dothat.ivr.extractor;

import com.dothat.ivr.data.IVRProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A Registry for URLs where Call Log Notifications are sent.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRDataExtractorRegistry {
  public static final Logger logger = LoggerFactory.getLogger(IVRDataExtractorRegistry.class);
  
  private static final IVRDataExtractorRegistry INSTANCE = new IVRDataExtractorRegistry();

  private final Map<IVRProvider, IVRDataExtractor.Factory> extractMap = new HashMap<>();
  
  private IVRDataExtractorRegistry() {
    extractMap.put(IVRProvider.MY_OPERATOR, new MyOperatorDataExtractor.Factory());
  }
  
  public static IVRDataExtractorRegistry getInstance() {
    return INSTANCE;
  }
  
  public IVRDataExtractor getExtractor(IVRProvider provider) {
    IVRDataExtractor.Factory factory = extractMap.get(provider);
    if (factory != null) {
      logger.debug("Create Extractor for " + provider);
      return factory.create();
    }
    logger.warn("No Extractor registered for " + provider);
    return null;
  }
}
