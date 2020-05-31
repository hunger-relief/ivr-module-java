package com.dothat.relief.request.task;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.task.RequestBroadcastProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Queue that Broadcasts the Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestBroadcastProcessorTaskGenerator extends QueueTaskGenerator<ReliefRequest> {
  
  private static final String PROCESSOR_QUEUE_NAME = "queue-request-broadcast";
  private static final String PROCESSOR_URI = "/task/request/broadcast/processor";
  private static final String PROCESSOR_MODULE_NAME = "default";
  private static final Long PROCESSOR_DELAY_MILLIS = 10000L; // 10 seconds delay

  public RequestBroadcastProcessorTaskGenerator() {
    super(PROCESSOR_QUEUE_NAME, PROCESSOR_URI, PROCESSOR_MODULE_NAME, PROCESSOR_DELAY_MILLIS);
  }
  
  @Override
  protected List<String> getParameterNames() {
    List<String> params = new ArrayList<>();
    params.add(RequestBroadcastProcessor.REQUEST_ID_PARAM_NAME);
    params.add(RequestBroadcastProcessor.REQUEST_UUID_PARAM_NAME);
    params.add(RequestBroadcastProcessor.REQUEST_SOURCE_ID_PARAM_NAME);
    return params;
  }
  
  @Override
  protected String getParameterValue(ReliefRequest data, String parameterName) {
    if (RequestBroadcastProcessor.REQUEST_ID_PARAM_NAME.equals(parameterName)) {
      return String.valueOf(data.getRequestId());
    } else if (RequestBroadcastProcessor.REQUEST_UUID_PARAM_NAME.equals(parameterName)) {
      return data.getRequesterID().getIdentifier();
    } else if (RequestBroadcastProcessor.REQUEST_SOURCE_ID_PARAM_NAME.equals(parameterName)) {
      return data.getSourceId();
    }
    return null;
  }
}
