package com.dothat.sync.task;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.profile.ProfileFieldExtractor;
import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.relief.provider.ReliefProviderService;
import com.dothat.relief.provider.data.ProviderConfig;
import com.dothat.sync.sheets.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
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

    // TODO(abhideep): Lookup all Providers who need to be send this broadcast or take that as an attribute.
    String providerCode = ReliefProviderService.DEFAULT;
    ProviderConfig config = new ReliefProviderService().getProviderConfig(providerCode);
    if (config == null || Strings.isNullOrEmpty(config.getGoogleSheetId())) {
      resp.sendError(500,"No Spreadsheet found for provider " + providerCode);
      return;
    }
    Sheets sheets;
    try {
      sheets = SheetsProvider.createSheetsService();
    } catch (GeneralSecurityException gse) {
      logger.error("Error Reading Spreadsheet ", gse);
      throw new IOException(gse);
    }

    String fieldName = attribute.getAttributeName();
    // TODO(abhideep): Decide between Requests and Shared Profiles
    String sheetName = "Profiles";
    List<String> fieldNames = new GetHeaderFromSheet(sheets)
        .getHeaders(config.getGoogleSheetId(), sheetName);
    String requestSheetName = "Requests";
    List<String> requestFieldNames = new GetHeaderFromSheet(sheets)
        .getHeaders(config.getGoogleSheetId(), requestSheetName);
    
    if (requestFieldNames.contains(fieldName)) {
      new UpdateCellTask(sheets).updateCell(
          config.getGoogleSheetId(), UpdateCellTaskConfig.forRequest(), attribute);
    }
    
    // Load All Attributes. If this is the earliest one, Create a new Row
    List<ProfileAttribute> attributes = service.lookupAllBySourceId(
        attribute.getIdentityUUID(), attribute.getSource(), attribute.getSourceId());
    
    if (attributes == null || isEarliestAttribute(attribute, attributes)) {
      logger.info("Found " + attributes.size() + " with source " + attribute.getSource()
          + " and source Id " + attribute.getSourceId());
      List<List<Object>> values = new ProfileFieldExtractor().extractValues(attribute);
      
      new AppendRowToSheet(sheets).appendRow(config.getGoogleSheetId(), AppendRowConfig.forProfile(), values);
      logger.info("Added row to Profiles Sheet");

      new UpdateCellTask(sheets).updateCell(
          config.getGoogleSheetId(), UpdateCellTaskConfig.forProfile(), attribute);
      logger.info("Updated cells on Profiles Sheet");
    } else {
      new UpdateCellTask(sheets).updateCell(
          config.getGoogleSheetId(), UpdateCellTaskConfig.forProfile(), attribute);
      logger.info("Updated cells on Profiles Sheet");
    }

    resp.setContentType("text/plain");
    resp.getWriter().println("Added 1 Row to the sheet " + sheetName);
    resp.flushBuffer();
  }
  
  boolean isEarliestAttribute(ProfileAttribute attribute, List<ProfileAttribute> attrList) {
    if (attrList == null || attrList.isEmpty()) {
      return true;
    }
    DateTime attributeTimestamp = JodaUtils.toDateTime(attribute.getTimestamp());
    for (ProfileAttribute attr : attrList) {
      DateTime attrTimestamp = JodaUtils.toDateTime(attr.getTimestamp());
      if (!attr.getAttributeId().equals(attribute.getAttributeId())
          && attrTimestamp.isBefore(attributeTimestamp)) {
        return false;
      }
    }
    return true;
  }
}
