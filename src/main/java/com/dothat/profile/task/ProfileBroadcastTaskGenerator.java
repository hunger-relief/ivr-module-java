package com.dothat.profile.task;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.sync.task.BroadcastProfileUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Queue that Broadcasts the Attribute Changes.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileBroadcastTaskGenerator extends QueueTaskGenerator<ProfileAttribute> {
  
  private static final String PROCESSOR_QUEUE_NAME = "queue-profile-broadcast";
  private static final String PROCESSOR_URI = "/task/profile/broadcast";
  private static final String PROCESSOR_MODULE_NAME = "default";
  private static final Long PROCESSOR_DELAY_MILLIS = 300000L; // 5 Min Delay
  
  public ProfileBroadcastTaskGenerator() {
    super(PROCESSOR_QUEUE_NAME, PROCESSOR_URI, PROCESSOR_MODULE_NAME, PROCESSOR_DELAY_MILLIS);
  }
  
  @Override
  protected List<String> getParameterNames() {
    List<String> params = new ArrayList<>();
    params.add(BroadcastProfileUpdate.ATTRIBUTE_ID_PARAM_NAME);
    params.add(BroadcastProfileUpdate.ATTRIBUTE_UUID_PARAM_NAME);
    params.add(BroadcastProfileUpdate.ATTRIBUTE_SOURCE_ID_PARAM_NAME);
    return params;
  }
  
  @Override
  protected String getParameterValue(ProfileAttribute data, String parameterName) {
    if (BroadcastProfileUpdate.ATTRIBUTE_ID_PARAM_NAME.equals(parameterName)) {
      return String.valueOf(data.getAttributeId());
    } else if (BroadcastProfileUpdate.ATTRIBUTE_UUID_PARAM_NAME.equals(parameterName)) {
      return data.getIdentityUUID().getIdentifier();
    } else if (BroadcastProfileUpdate.ATTRIBUTE_SOURCE_ID_PARAM_NAME.equals(parameterName)) {
      return data.getSourceId();
    }
    return null;
  }
}
