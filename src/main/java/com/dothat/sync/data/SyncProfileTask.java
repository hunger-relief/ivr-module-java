package com.dothat.sync.data;

import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.relief.request.data.RequestType;
import com.dothat.sync.destination.data.Destination;

/**
 * Data for a Request that needs to be Synced to a Sheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SyncProfileTask {
  private Long taskId;
  
  private String processTaskName;
  private SyncProcessType processType;

  private ReliefProvider provider;
  private RequestType requestType;
  private Destination destination;

  private ProfileAttribute profileAttribute;
  
  public Long getTaskId() {
    return taskId;
  }
  
  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }
  
  public String getProcessTaskName() {
    return processTaskName;
  }
  
  public void setProcessTaskName(String processTaskName) {
    this.processTaskName = processTaskName;
  }
  
  public SyncProcessType getProcessType() {
    return processType;
  }
  
  public void setProcessType(SyncProcessType processType) {
    this.processType = processType;
  }
  
  public ReliefProvider getProvider() {
    return provider;
  }
  
  public void setProvider(ReliefProvider provider) {
    this.provider = provider;
  }
  
  public RequestType getRequestType() {
    return requestType;
  }
  
  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }
  
  public Destination getDestination() {
    return destination;
  }
  
  public void setDestination(Destination destination) {
    this.destination = destination;
  }
  
  public ProfileAttribute getProfileAttribute() {
    return profileAttribute;
  }
  
  public void setProfileAttribute(ProfileAttribute profileAttribute) {
    this.profileAttribute = profileAttribute;
  }
}
