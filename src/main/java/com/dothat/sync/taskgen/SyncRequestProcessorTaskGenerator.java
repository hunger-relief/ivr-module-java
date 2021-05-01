package com.dothat.sync.taskgen;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.sync.data.SyncProcessType;
import com.dothat.sync.data.SyncRequestTask;
import com.dothat.sync.task.SyncRequestProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Queue that Syncs the Request to a Google Sheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncRequestProcessorTaskGenerator extends QueueTaskGenerator<SyncRequestTask> {
  
  public static final String PROCESSOR_QUEUE_NAME = "queue-request-sync";
  private static final String PROCESSOR_URI = "/task/request/sync";
  private static final String PROCESSOR_MODULE_NAME = "default";
  private static final Long PROCESSOR_DELAY_MILLIS = 300000L; // 5 minute delay
  
  public SyncRequestProcessorTaskGenerator(String taskName, Long delayMillis) {
    super(PROCESSOR_QUEUE_NAME, taskName, PROCESSOR_URI, PROCESSOR_MODULE_NAME,
        delayMillis == null ? PROCESSOR_DELAY_MILLIS : delayMillis);
  }
  
  @Override
  protected List<String> getParameterNames() {
    List<String> params = new ArrayList<>();
    params.add(SyncRequestProcessor.PROVIDER_CODE_PARAM_NAME);
    params.add(SyncRequestProcessor.SYNC_TYPE_PARAM_NAME);
    params.add(SyncRequestProcessor.PROCESS_TASK_PARAM_NAME);
    return params;
  }
  
  @Override
  protected String getParameterValue(SyncRequestTask data, String parameterName) {
    if (SyncRequestProcessor.PROVIDER_CODE_PARAM_NAME.equals(parameterName)) {
      return data.getProvider().getProviderCode();
    } else if (SyncRequestProcessor.SYNC_TYPE_PARAM_NAME.equals(parameterName)) {
      return SyncProcessType.REQUEST.name();
    } else if (SyncRequestProcessor.PROCESS_TASK_PARAM_NAME.equals(parameterName)) {
      return data.getProcessTaskName();
    }
    return null;
  }
}
