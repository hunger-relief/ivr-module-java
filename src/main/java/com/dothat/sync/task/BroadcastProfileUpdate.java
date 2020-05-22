package com.dothat.sync.task;

import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.request.ReliefRequestService;
import com.dothat.relief.request.data.ReliefRequest;
import com.dothat.sync.destination.DestinationService;
import com.dothat.sync.destination.data.Destination;
import com.dothat.sync.destination.data.DestinationType;
import com.dothat.sync.sheets.SheetsProvider;
import com.google.api.services.sheets.v4.Sheets;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Syncs the Profile Update to the Sheet for the Relief Provider.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class BroadcastProfileUpdate extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(BroadcastProfileUpdate.class);
  
  public static final String ATTRIBUTE_ID_PARAM_NAME = "attributeId";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String attributeIdParam = req.getParameter(ATTRIBUTE_ID_PARAM_NAME);
    if (Strings.isNullOrEmpty(attributeIdParam)) {
      logger.error("Attribute Id not specified");
      resp.sendError(400, "Attribute Id not specified");
      return;
    }
    ProfileService service = new ProfileService();
    Long attributeId = Long.valueOf(attributeIdParam);
    ProfileAttribute attribute = service.lookupById(attributeId);
    if (attribute == null) {
      logger.error("No Attribute found for Id {} ", attributeId);
      resp.sendError(404, "No Attribute found for Id " + attributeId);
      return;
    }

    // Lookup the list of Requests that this is relevant for
    // TODO(abhideep): Add List of Profile Collection Request Initiator .
    ReliefRequest request = new ReliefRequestService().lookupLastRequest(
        attribute.getIdentityUUID(), attribute.getSourceType(), attribute.getSource());
    if (request == null) {
      logger.warn("No Request found for {} from {} {} ",
          attribute.getIdentityUUID().getIdentifier(), attribute.getSourceType(), attribute.getSource());
      resp.sendError(500,"No Request found for "
          + attribute.getIdentityUUID().getIdentifier()
          + " from " + attribute.getSourceType() + " " + attribute.getSource());
    }
    // TODO(abhideep): Lookup all Providers who need to be sent this broadcast.
    // TODO(abhideep): Add Support for Multiple Requests and Destinations
    Destination destination = new DestinationService()
        .lookupDestination(request.getProvider(), request.getRequestType(), request.getLocation(),
            DestinationType.GOOGLE_SHEETS);
    
    if (destination == null || Strings.isNullOrEmpty(destination.getGoogleSheetId())) {
      logger.warn("No Destination found for request for {} from {} {} ",
          attribute.getIdentityUUID().getIdentifier(), request.getSourceType(), request.getSource());
      resp.sendError(500,"No Destination found for request for "
          + attribute.getIdentityUUID().getIdentifier()
          + " from " + request.getSourceType() + " " + request.getSource());
      return;
    }
    Sheets sheets;
    try {
      sheets = SheetsProvider.createSheetsService();
    } catch (GeneralSecurityException gse) {
      logger.error("Error Reading Spreadsheet ", gse);
      throw new IOException(gse);
    }

    // Update the Cell for
    new UpdateAttributeTask(sheets, destination.getGoogleSheetId()).update(attribute);

    resp.setContentType("text/plain");
    resp.getWriter().println("Added 1 Row to the sheet Profiles");
    resp.flushBuffer();
  }
}
