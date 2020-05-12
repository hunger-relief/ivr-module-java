package com.dothat.profile;

import com.dothat.profile.data.ProfileAttribute;
import com.dothat.profile.store.ProfileStore;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileService {
  private final ProfileStore store;
  
  public ProfileService() {
    this.store = new ProfileStore();
  }
  
  public Long save(ProfileAttribute data) {
    return store.store(data, null);
  }
}
