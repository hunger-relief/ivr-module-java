package com.dothat.sync.taskgen;

import com.dothat.common.queue.QueueTaskGenerator;
import com.dothat.sync.data.SyncProfileTask;
import com.dothat.sync.task.SyncProfileProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a Task that in added to the Queue that Syncs the Request to a Google Sheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncProfileProcessorTaskGenerator extends QueueTaskGenerator<SyncProfileTask> {
  
  private static final String PROCESSOR_QUEUE_NAME = "queue-profile-sync";
  private static final String PROCESSOR_URI = "/task/profile/sync";
  private static final String PROCESSOR_MODULE_NAME = "default";
  private static final Long PROCESSOR_DELAY_MILLIS = 300000L; // 5 minute delay
  
  public SyncProfileProcessorTaskGenerator(String taskName) {
    super(PROCESSOR_QUEUE_NAME, taskName, PROCESSOR_URI, PROCESSOR_MODULE_NAME, PROCESSOR_DELAY_MILLIS);
  }
  
  @Override
  protected List<String> getParameterNames() {
    List<String> params = new ArrayList<>();
    params.add(SyncProfileProcessor.PROVIDER_CODE_PARAM_NAME);
    params.add(SyncProfileProcessor.REQUEST_TYPE_PARAM_NAME);
    params.add(SyncProfileProcessor.PROCESS_TASK_PARAM_NAME);
    return params;
  }
  
  @Override
  protected String getParameterValue(SyncProfileTask data, String parameterName) {
    if (SyncProfileProcessor.PROVIDER_CODE_PARAM_NAME.equals(parameterName)) {
      return data.getProvider().getProviderCode();
    } else if (SyncProfileProcessor.REQUEST_TYPE_PARAM_NAME.equals(parameterName)) {
      return data.getRequestType().name();
    } else if (SyncProfileProcessor.PROCESS_TASK_PARAM_NAME.equals(parameterName)) {
      return data.getProcessTaskName();
    }
    return null;
  }
}
