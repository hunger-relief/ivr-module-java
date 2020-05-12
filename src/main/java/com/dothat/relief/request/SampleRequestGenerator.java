package com.dothat.relief.request;

import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.location.data.Country;
import com.dothat.location.data.Location;
import com.dothat.location.impl.IndiaState;
import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ReliefProvider;
import com.dothat.relief.request.data.*;

/**
 * Generates a Sample Relief Request for testing.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SampleRequestGenerator {
  
  public ReliefRequest generate(String phoneNumber, RequestType requestType) {
    ReliefRequest data = new ReliefRequest();
  
    Location location = new Location();
    location.setCountry(Country.INDIA);
    location.setState(IndiaState.HARYANA);
    location.setCity("Gurgaon");
    location.setLocation("Sarhol");
  
    ObfuscatedID id = new IdentityService().registerNumber(phoneNumber);
    ReliefProvider provider = new ReliefProviderService().assignProvider(id, requestType, location);
    data.setProvider(provider);
    data.setRequesterID(id);
    data.setLocation(location);
    data.setRequestType(requestType);
    data.setClaimStatus(ClaimStatus.CLAIMED);
    data.setVerificationStatus(VerificationStatus.UNVERIFIED);
    data.setRequestStatus(RequestStatus.RECEIVED);
    return data;
  }
}
