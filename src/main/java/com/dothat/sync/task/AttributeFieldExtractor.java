package com.dothat.sync.task;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the Attribute Data to a Composer to include in the Sheet that is written out.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class AttributeFieldExtractor {
  private static final Logger logger = LoggerFactory.getLogger(AttributeFieldExtractor.class);
  
  private final ObfuscatedID obfuscatedID;
  
  public AttributeFieldExtractor(ObfuscatedID obfuscatedID) {
    this.obfuscatedID = obfuscatedID;
  }
  
  Map<String, String> getAttributeDataMap() {
    Map<String, String> attributeMap = new HashMap<>();

    ProfileService service = new ProfileService();
    List<ProfileAttribute> attributeList = service.lookupByUUID(obfuscatedID);
    if (attributeList == null) {
      logger.info("No existing Attributes found - Potentially a first time user in the system");
      return attributeMap;
    }
    attributeList.sort(new AttributeSorter());
  
    // Sort all Attributes and then add them as fields as well.
    for (ProfileAttribute attribute : attributeList) {
      if (!Strings.isNullOrEmpty(attribute.getAttributeValue())) {
        attributeMap.put(attribute.getAttributeName(), attribute.getAttributeValue());
      }
    }
    return attributeMap;
  }
  
  /**
   * Sorts the Attributes based on Timestamp when the data was received.
   */
  private static class AttributeSorter implements Comparator<ProfileAttribute> {
    @Override
    public int compare(ProfileAttribute lhs, ProfileAttribute rhs) {
      DateTime lhsTimestamp = JodaUtils.toDateTime(lhs.getTimestamp());
      DateTime rhsTimestamp = JodaUtils.toDateTime(rhs.getTimestamp());
      return lhsTimestamp.compareTo(rhsTimestamp);
    }
  }
}
