package com.dothat.ivr.notif.task;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.ivr.notif.data.IVRCall;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Call Processing Queue.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRCallProcessorTaskGenerator extends QueueTaskGenerator<IVRCall> {
  
  public static final String PROCESSOR_QUEUE_NAME = "queue-call-processor";
  public static final String PROCESSOR_URI = "/task/call/processor";
  public static final String PROCESSOR_MODULE_NAME = "default";

  
  public IVRCallProcessorTaskGenerator() {
    super(PROCESSOR_QUEUE_NAME, PROCESSOR_URI, PROCESSOR_MODULE_NAME);
  }
  
  @Override
  protected List<String> getParameterNames() {
    List<String> params = new ArrayList<>();
    params.add(IVRCallProcessor.CALL_ID_PARAM_NAME);
    return params;
  }
  
  @Override
  protected String getParameterValue(IVRCall data, String parameterName) {
    if (IVRCallProcessor.CALL_ID_PARAM_NAME.equals(parameterName)) {
      return String.valueOf(data.getCallId());
    }
    return null;
  }
}
