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
import java.util.Collections;
import java.util.List;

/**
 * Extract all the fields fromm a Relief Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileFieldExtractor {
  
  public List<List<Object>> extractValues(ProfileAttribute data) {
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

    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MMM dd hh:mm")
        .withZone(CountryTimeZoneLookup.getInstance().getTimeZone(country));
    row.add(dateFormatter.print(timestamp));
    
    if (data.getIdentityUUID() != null) {
      row.add(data.getIdentityUUID().getIdentifier());
    }
    
    addString(row, ProfileField.PHONE, phoneNumber);
    addString(row, ProfileField.SOURCE, data.getSource());
    addString(row, ProfileField.SOURCE_ID, data.getSourceId());
    return Collections.singletonList(row);
  }
  
  void addString(List<Object> row, ProfileField field, String value) {
    if (value != null) {
      row.add(value);
    }
  }
}
