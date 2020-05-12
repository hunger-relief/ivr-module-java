package com.dothat.ivr.notif.task;

import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.ivr.mapping.IVRNodeMappingService;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.request.data.SourceType;

/**
 * Generates a Profile Attribute based on Call Node Notification.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileAttributeGenerator {
  private final IdentityService identityService = new IdentityService();
  
  ProfileAttribute generate(IVRCallNode node) {
    ProfileAttribute data = new ProfileAttribute();
  
    IVRNodeMappingService mappingService = new IVRNodeMappingService();
    
    data.setAttributeName(mappingService.getAttributeName(node.getProviderNodeId(), node.getProvider()));
    data.setAttributeValue(mappingService.getAttributeValue(node.getKeyPress(), node.getProviderNodeId(),
        node.getProvider()));
    
    data.setSourceType(SourceType.IVR);
    if (node.getProvider() != null) {
      data.setSource(node.getProvider().name());
    }
    data.setSourceId(node.getProviderCallId());

    // First get the UUID
    ObfuscatedID obfId = identityService.registerNumber(node.getCallerNumber());
    data.setIdentityUUID(obfId);
    
    data.setTimestamp(node.getTimestamp());
    
    data.setCreationTimestamp(node.getCreationTimestamp());
    data.setModificationTimestamp(node.getCreationTimestamp());

    return data;
  }
}
