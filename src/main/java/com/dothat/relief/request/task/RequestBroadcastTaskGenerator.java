package com.dothat.relief.request.task;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.task.BroadcastReliefRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Call Processing Queue.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestBroadcastTaskGenerator extends QueueTaskGenerator<ReliefRequest> {
  
  public static final String PROCESSOR_QUEUE_NAME = "queue-request-processor";
  public static final String PROCESSOR_URI = "/task/request/broadcast";
  public static final String PROCESSOR_MODULE_NAME = "default";

  
  public RequestBroadcastTaskGenerator() {
    super(PROCESSOR_QUEUE_NAME, PROCESSOR_URI, PROCESSOR_MODULE_NAME);
  }
  
  @Override
  protected List<String> getParameterNames() {
    List<String> params = new ArrayList<>();
    params.add(BroadcastReliefRequest.REQUEST_ID_PARAM_NAME);
    return params;
  }
  
  @Override
  protected String getParameterValue(ReliefRequest data, String parameterName) {
    if (BroadcastReliefRequest.REQUEST_ID_PARAM_NAME.equals(parameterName)) {
      return String.valueOf(data.getRequestId());
    }
    return null;
  }
}
