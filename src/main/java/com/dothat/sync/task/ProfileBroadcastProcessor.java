package com.dothat.sync.task;

import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.sync.SyncService;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Syncs the Profile Update to the Sheet for the Relief Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class ProfileBroadcastProcessor extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(ProfileBroadcastProcessor.class);
  
  public static final String ATTRIBUTE_ID_PARAM_NAME = "attributeId";
  public static final String ATTRIBUTE_UUID_PARAM_NAME = "attributeUUID";
  public static final String ATTRIBUTE_SOURCE_ID_PARAM_NAME = "attributeSourceId";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String attributeIdValue = req.getParameter(ATTRIBUTE_ID_PARAM_NAME);
    if (Strings.isNullOrEmpty(attributeIdValue)) {
      logger.error("Attribute Id not specified");
      resp.sendError(400, "Attribute Id not specified");
      return;
    }
    ProfileService service = new ProfileService();
    Long attributeId = Long.valueOf(attributeIdValue);
    ProfileAttribute attribute = service.lookupById(attributeId);
    if (attribute == null) {
      String attributeUUID = req.getParameter(ATTRIBUTE_UUID_PARAM_NAME);
      String attributeSourceId = req.getParameter(ATTRIBUTE_SOURCE_ID_PARAM_NAME);
      logger.error("No Attribute found with Id {} from {} with Source ID {} ", attributeIdValue,
          attributeUUID, attributeSourceId);
      resp.sendError(404, "No Attribute found with Id " + attributeIdValue
          + " from " + attributeUUID + " with Source ID " + attributeSourceId);
      return;
    }
  
    Long taskId = new SyncService().createSyncProfileTask(attribute);
  
    resp.setContentType("text/plain");
    resp.getWriter().println("Created Sync Task " + taskId
        + " for profile for " + attribute.getIdentityUUID().getIdentifier()
        + " from " + attribute.getSourceType() + " " + attribute.getSource()
        + " with Source ID " + attribute.getSourceId() );
    resp.flushBuffer();
  }
}
