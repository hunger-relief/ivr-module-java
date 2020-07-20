package com.dothat.relief.request.task;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.task.BroadcastReliefRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Queue that Broadcasts the Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestBroadcastTaskGenerator extends QueueTaskGenerator<ReliefRequest> {
  
  private static final String PROCESSOR_QUEUE_NAME = "queue-request-processor";
  private static final String PROCESSOR_URI = "/task/request/broadcast/realtime";
  private static final String PROCESSOR_MODULE_NAME = "default";
  private static final Long PROCESSOR_DELAY_MILLIS = 1000L;

  
  public RequestBroadcastTaskGenerator() {
    super(PROCESSOR_QUEUE_NAME, PROCESSOR_URI, PROCESSOR_MODULE_NAME, PROCESSOR_DELAY_MILLIS);
  }
  
  @Override
  protected List<String> getParameterNames() {
    List<String> params = new ArrayList<>();
    params.add(BroadcastReliefRequest.REQUEST_ID_PARAM_NAME);
    params.add(BroadcastReliefRequest.REQUEST_UUID_PARAM_NAME);
    params.add(BroadcastReliefRequest.REQUEST_SOURCE_ID_PARAM_NAME);
    return params;
  }
  
  @Override
  protected String getParameterValue(ReliefRequest data, String parameterName) {
    if (BroadcastReliefRequest.REQUEST_ID_PARAM_NAME.equals(parameterName)) {
      return String.valueOf(data.getRequestId());
    } else if (BroadcastReliefRequest.REQUEST_UUID_PARAM_NAME.equals(parameterName)) {
      return data.getRequesterID().getIdentifier();
    } else if (BroadcastReliefRequest.REQUEST_SOURCE_ID_PARAM_NAME.equals(parameterName)) {
      return data.getSourceId();
    }
    return null;
  }
}
