package com.dothat.relief.request;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.relief.request.data.ReliefRequest;
import org.joda.time.DateTime;

import java.util.Comparator;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class RequestSorter implements Comparator<ReliefRequest> {
  private final int multiplier;
  
  public RequestSorter() {
    this(false);
  }
  
  public RequestSorter(boolean earliestFirst) {
    this.multiplier = earliestFirst ? 1 : -1;
  }
  
  @Override
  public int compare(ReliefRequest lhsRequest, ReliefRequest rhsRequest) {
    DateTime lhs = JodaUtils.toDateTime(lhsRequest.getRequestTimestamp());
    DateTime rhs = JodaUtils.toDateTime(rhsRequest.getRequestTimestamp());
    
    if (lhs != null && rhs != null) {
      return (multiplier * lhs.compareTo(rhs));
    } else if (lhs == null && rhs == null) {
      return (multiplier * (JodaUtils.toDateTime(lhsRequest.getCreationTimestamp())
          .compareTo(JodaUtils.toDateTime(rhsRequest.getCreationTimestamp()))));
    } else if (lhs != null) {
      return multiplier;
    }
    return (-1 * multiplier);
  }
}
