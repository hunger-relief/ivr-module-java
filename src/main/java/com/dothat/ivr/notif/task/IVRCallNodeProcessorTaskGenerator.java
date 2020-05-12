package com.dothat.ivr.notif.task;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.ivr.notif.data.IVRCallNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Call Node Processing Queue.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRCallNodeProcessorTaskGenerator extends QueueTaskGenerator<IVRCallNode> {
  
  public static final String PROCESSOR_QUEUE_NAME = "queue-node-processor";
  public static final String PROCESSOR_URI = "/task/node/processor";
  public static final String PROCESSOR_MODULE_NAME = "default";
  
  public IVRCallNodeProcessorTaskGenerator() {
    super(PROCESSOR_QUEUE_NAME, PROCESSOR_URI, PROCESSOR_MODULE_NAME);
  }
  
  @Override
  protected List<String> getParameterNames() {
    List<String> params = new ArrayList<>();
    params.add(IVRCallNodeProcessor.CALL_NODE_ID_PARAM_NAME);
    return params;
  }
  
  @Override
  protected String getParameterValue(IVRCallNode data, String parameterName) {
    if (IVRCallNodeProcessor.CALL_NODE_ID_PARAM_NAME.equals(parameterName)) {
      return String.valueOf(data.getCallNodeId());
    }
    return null;
  }
}
