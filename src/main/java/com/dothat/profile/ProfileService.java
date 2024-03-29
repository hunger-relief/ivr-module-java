package com.dothat.profile;

import com.dothat.identity.data.ObfuscatedID;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.profile.store.ProfileStore;
import com.dothat.profile.task.ProfileBroadcastTaskGenerator;
import com.dothat.relief.request.data.SourceType;

import java.util.List;

/**
 * Service Layer to manage Profiles of users.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileService {
  private final ProfileStore store;
  
  public ProfileService() {
    this.store = new ProfileStore();
  }
  
  public Long save(ProfileAttribute data) {
    return store.store(data, new ProfileBroadcastTaskGenerator());
  }
  
  public ProfileAttribute lookupById(Long attributeId) {
    return store.find(attributeId);
  }
  
  public List<ProfileAttribute> lookupAllBySourceId(ObfuscatedID obfuscatedId, SourceType sourceType,
                                                    String source, String sourceId) {
    return store.findAllForSource(obfuscatedId.getIdentifier(), sourceType, source, sourceId);
  }
  
  public List<ProfileAttribute> lookupAllByAttributeName(ObfuscatedID obfuscatedId, String attributeName) {
    return store.findAllForName(obfuscatedId.getIdentifier(), attributeName);
  }
  
  public List<ProfileAttribute> lookupByUUID(ObfuscatedID obfuscatedId) {
    return store.findAllForUUID(obfuscatedId.getIdentifier());
  }
}
