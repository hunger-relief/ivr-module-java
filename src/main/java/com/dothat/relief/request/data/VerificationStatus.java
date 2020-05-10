package com.dothat.relief.request.data;

/**
 * Enum for the Verification Status of the Request.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum VerificationStatus {
  UNVERIFIED,

  INFO_REQUESTED,
  INFO_RECEIVED,

  REVIEW_REQUESTED,
  REVIEW_PROCESSING,
  
  VERIFIED,
  FLAGGED,
  ;
}
