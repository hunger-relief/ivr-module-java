package com.dothat.identity;

import com.dothat.common.validate.PhoneNumberValidator;
import com.dothat.identity.data.ExternalID;
import com.dothat.identity.data.IdSourceType;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.identity.store.IdentityStore;
import com.dothat.identity.store.IdentityStoreImpl;

import java.util.UUID;

/**
 * Service Layer to lookup and store unique and obfuscated identifiers for a person who is currently identified
 * by other means like a phone Number.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IdentityService {
  private final IdentityStore store;

  public IdentityService() {
    this(new IdentityStoreImpl());
  }

  IdentityService(IdentityStore store) {
    this.store = store;
  }
  
  /**
   * Register a phone number into the system or return the existing obfuscated id if the number is already
   * registered.
   *
   * @param phoneNumber Phone number including the country code
   * @return Obfuscated Id for the Phone Number
   */
  public ObfuscatedID registerNumber(String phoneNumber) {
    ObfuscatedID id = store.load(IdSourceType.PHONE_NUMBER, phoneNumber);
    if (id != null) {
      return id;
    }
    // Validate the number before saving it
    PhoneNumberValidator validator = new PhoneNumberValidator();
    validator.validate(phoneNumber);

    // Generate a UUID as the obfuscated ID for the phone number
    String obfuscatedId = UUID.randomUUID().toString();
  
    // Associate the Obfuscated ID with the Phone number, and save both in the system
    ExternalID data = new ExternalID();
    data.setSourceType(IdSourceType.PHONE_NUMBER);
    data.setExternalId(phoneNumber);
    data.setObfuscatedId(obfuscatedId);
    return store.store(data);
  }
}
