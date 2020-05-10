package com.dothat.common.queue;

/**
 * @author abhideep@ (Abhideep Singh)
 */

import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.util.List;

/**
 * Generates a Task for processing in an AppEngine Queue.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public abstract class QueueTaskGenerator<T> implements TaskGenerator<T> {
  
  private final String queueName;
  private final String processorUri;
  private final String moduleName;
  
  public QueueTaskGenerator(String queueName, String processorUri, String moduleName) {
    this.queueName = queueName;
    this.processorUri = processorUri;
    this.moduleName = moduleName;
  }
  
  @Override
  public void generateTask(T data) {
    Queue queue = QueueFactory.getQueue(queueName);
    TaskOptions options = TaskOptions.Builder.withUrl(processorUri)
        .method(TaskOptions.Method.GET)
        .header("Host", ModulesServiceFactory.getModulesService()
            .getVersionHostname(moduleName, null));
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