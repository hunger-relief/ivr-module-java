package com.dothat.profile;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.common.time.CountryTimeZoneLookup;
import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ExternalID;
import com.dothat.location.data.Country;
import com.dothat.profile.data.ProfileAttribute;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extract all the fields fromm a Relief Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileFieldExtractor {
  
  public Map<String, String> extractValues(ProfileAttribute data) {
    Map<String, String> map = new HashMap<>();

    ExternalID number = new IdentityService().lookupNumberById(data.getIdentityUUID());
    String phoneNumber = number ==  null ? "" : number.getExternalId();
    // Strip out the country code if any and add ' to make it a string literal
    int index = phoneNumber.indexOf("-");
    phoneNumber = "'" + phoneNumber.substring(index + 1);

    List<Object> row = new ArrayList<>();
    DateTime timestamp = JodaUtils.toDateTime(data.getCreationTimestamp());
    if (timestamp == null) {
      timestamp = DateTime.now();
    }
    // TODO(abhideep): Figure this out based on ObfuscatedId
    Country country = Country.INDIA;

    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMM dd hh:mm a")
        .withZone(CountryTimeZoneLookup.getInstance().getTimeZone(country));
  
    map.put(ProfileField.TIMESTAMP.name(), dateFormatter.print(timestamp));
    map.put(ProfileField.PHONE.name(), phoneNumber);
    
    if (data.getIdentityUUID() != null) {
      map.put(ProfileField.UUID.name(), data.getIdentityUUID().getIdentifier());
    }
    map.put(ProfileField.ROW_NUM.name(), "=ROW()");
    map.put(ProfileField.SYNC_STATUS.name(), "Unsynced");
  
    map.put(ProfileField.SOURCE_TYPE.name(), data.getSourceType().name());
    map.put(ProfileField.SOURCE.name(), data.getSource());
    map.put(ProfileField.SOURCE_ID.name(), data.getSourceId());
  
    return map;
  }
}
