package com.dothat.common.queue;

/**
 * Interface for a class that generates a task for asynchronous processing based on the given data
 *
 * @author abhideep@ (Abhideep Singh)
 */
public interface TaskGenerator<T> {
  
  void generateTask(T data);
}
