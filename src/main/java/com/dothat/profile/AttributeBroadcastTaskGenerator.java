package com.dothat.profile;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.sync.task.BroadcastProfileUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Call Processing Queue.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class AttributeBroadcastTaskGenerator extends QueueTaskGenerator<ProfileAttribute> {
  
  public static final String PROCESSOR_QUEUE_NAME = "queue-attribute-processor";
  public static final String PROCESSOR_URI = "/task/attribute/broadcast";
  public static final String PROCESSOR_MODULE_NAME = "default";
  public static final Long PROCESSOR_DELAY_MILLIS = 10000L;

  
  public AttributeBroadcastTaskGenerator() {
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
