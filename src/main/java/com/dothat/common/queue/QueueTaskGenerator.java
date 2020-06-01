package com.dothat.common.queue;

import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.base.Strings;

import java.util.List;

/**
 * Generates a Task for processing in an AppEngine Queue.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public abstract class QueueTaskGenerator<T> implements TaskGenerator<T> {
  
  private final String queueName;
  private final String taskName;
  private final String processorUri;
  private final String moduleName;
  private final Long delayMillis;
  
  public QueueTaskGenerator(String queueName, String taskName, String processorUri, String moduleName,
                            Long delayMillis) {
    this.queueName = queueName;
    this.taskName = taskName;
    this.processorUri = processorUri;
    this.moduleName = moduleName;
    this.delayMillis = delayMillis;
  }
  
  public QueueTaskGenerator(String queueName, String processorUri, String moduleName, Long delayMillis) {
    this(queueName, null, processorUri, moduleName, delayMillis);
  }
  
  public QueueTaskGenerator(String queueName, String processorUri, String moduleName) {
    this(queueName, null, processorUri, moduleName, null);
  }
  
  @Override
  public void generateTask(T data) {
    Queue queue = QueueFactory.getQueue(queueName);
    TaskOptions options = TaskOptions.Builder.withUrl(processorUri)
        .method(TaskOptions.Method.GET)
        .header("Host", ModulesServiceFactory.getModulesService()
            .getVersionHostname(moduleName, null));
    
    if (delayMillis != null && delayMillis > 0) {
      options.countdownMillis(delayMillis);
    }

    if (!Strings.isNullOrEmpty(taskName)) {
      options.taskName(taskName);
    }

    List<String> params = getParameterNames();
    if (params != null) {
      for (String paramName : params) {
        String paramValue = getParameterValue(data, paramName);
        
        if (paramName != null && paramValue != null) {
          options = options.param(paramName, paramValue);
        }
      }
    }
    queue.add(options);
  }
  
  protected abstract List<String> getParameterNames();
  
  protected abstract String getParameterValue(T data, String parameterName);
}