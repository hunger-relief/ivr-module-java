package com.dothat.ivr.notif.task;

import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.ivr.mapping.IVRNodeMappingService;
import com.dothat.ivr.mapping.data.IVRNodeMapping;
import com.dothat.ivr.notif.data.IVRCallNode;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.request.data.SourceType;
import com.google.common.base.Strings;

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
  
    IVRNodeMapping mapping = mappingService.findMatch(node.getProvider(),  node.getIvrNumber(),
        node.getProviderNodeId(), node.getKeyPress());
    if (mapping == null) {
      data.setAttributeName(node.getProviderNodeId());
      data.setAttributeValue(node.getKeyPress());
    } else {
      // If there is a mapping but it has no value, the keyPress is the value.
      data.setAttributeName(mapping.getAttributeName());
      if (Strings.isNullOrEmpty(mapping.getAttributeValue()) && mapping.getLocation() == null
          && mapping.getRequestType() == null) {
        data.setAttributeValue(node.getKeyPress());
      } else {
        data.setAttributeValue(mapping.getAttributeValue());
      }
    }
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
