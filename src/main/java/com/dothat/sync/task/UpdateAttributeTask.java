package com.dothat.sync.task;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.identity.IdentityService;
import com.dothat.identity.data.ExternalID;
import com.dothat.identity.data.ObfuscatedID;
import com.dothat.profile.ProfileService;
import com.dothat.profile.data.ProfileAttribute;
import com.dothat.sync.sheets.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Update the Attribute Value in a Sheet
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class UpdateAttributeTask {
  private static final Logger logger = LoggerFactory.getLogger(UpdateAttributeTask.class);

  private final Sheets sheets;
  private final String spreadsheetId;
  
  public UpdateAttributeTask(Sheets sheets, String spreadsheetId) {
    this.sheets = sheets;
    this.spreadsheetId = spreadsheetId;
  }
  
  public void update(ProfileAttribute attribute) throws IOException {
    String attributeName = attribute.getAttributeName();
    // TODO(abhideep): Copy previous Attribute value if needed.
  
    
    // Add a Row to Profile Sheet if needed. Only done for the Profile Attributes tha was created earliest.
    ProfileService service = new ProfileService();
    List<ProfileAttribute> attributes = service.lookupAllBySourceId(
        attribute.getIdentityUUID(), attribute.getSourceType(), attribute.getSource(), attribute.getSourceId());

    // Not sure how this is even possible, but just a fail-safe to make it easier to write code ahead.
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    logger.info("Found {} attributes with source {} and source Id {} for source type {}",
        attributes.size(), attribute.getSource(), attribute.getSourceId(), attribute.getSourceType());

    if (isEarliestAttribute(attribute, attributes)) {
      SheetHeader profilesHeader = LookupSheetHeader.forProfiles(sheets, spreadsheetId).getHeader();
      LookupRowResult newRowResult = LookupRow.forProfiles(sheets).lookup(spreadsheetId, profilesHeader);
      Integer cellRowNumber = newRowResult.getRowForSource(attribute.getSourceType(), attribute.getSource(),
          attribute.getSourceId());
      // We add a row only if a row doesn't already exist for the Source Type, Source, and Source Id
      if (cellRowNumber == null) {
        List<List<Object>> values = new ProfileRowComposer(profilesHeader.getRowValues()).compose(attribute,
            new AttributeFieldExtractor(attribute.getIdentityUUID()));
        new AppendRowToSheet(sheets).appendRow(spreadsheetId, AppendRowConfig.forProfile(), values);
        logger.info("Added row to Profiles Sheet");
      }
    }
  
    // Load the the first Row of the Profiles Sheet - This will tell you which column
    // has the data for
    SheetHeader header = LookupSheetHeader.forProfiles(sheets, spreadsheetId).getHeader();
    String cellColumnLabel = header.getColumnLabel(attributeName);
    if (cellColumnLabel == null) {
      logger.info("No column found for Attribute {} in the Profiles Sheet", attributeName);
      throw new IllegalStateException("No column found for Attribute " + attributeName + " in the Profiles Sheet");
    }
    // Load Columns that have the unique key for the Profiles
    LookupRowResult rowResult = LookupRow.forProfiles(sheets).lookup(spreadsheetId, header);
    Integer cellRowNumber = rowResult.getRowForSource(attribute.getSourceType(), attribute.getSource(),
        attribute.getSourceId());
    
    if (cellRowNumber == null) {
      logger.info("No Row found with Source Type {} source {} and source Id {} while trying to update Attribute {} " +
          "in the Profiles Sheet", attribute.getSourceType(), attribute.getSource(), attribute.getSourceId(),
          attributeName);
      throw new IllegalStateException("No Row found with Source Type " + attribute.getSourceType() + " source "
          + attribute.getSource() + " and source Id " + attribute.getSourceId() + " while trying to update Attribute "
          + attributeName + " in the Profiles Sheet");
    }
    
    // Update the Cell which has the Attribute Value
    String cellRange = "Profiles!" + cellColumnLabel + cellRowNumber;
    String cellValue = getCellValue(attribute);
    logger.info("Updating Cell {} with Value {} for Attribute {} in the Profiles Sheet",
        cellRange, cellValue, attributeName);
    updateCellValue(cellValue, cellRange);
  
    // If the Request Sheet has a column with the same name, update that as well
    SheetHeader requestHeaders = LookupSheetHeader.forRequests(sheets, spreadsheetId).getHeader();
    if (requestHeaders.getColumnNumber(attributeName) != null) {
      String phoneNumber = SheetDataSanitizer.sanitizePhoneNumber(getPhoneNumber(attribute));
      if (phoneNumber.startsWith("'")) {
        phoneNumber = phoneNumber.substring(1);
      }

      LookupRowResult requestRow = LookupRow.forRequests(sheets).lookup(spreadsheetId, requestHeaders);
      Integer requestRowNumber = requestRow.getLastRowForPhone(phoneNumber, attribute.getTimestamp());
      if (requestRowNumber == null) {
        logger.info("No Row found with Phone number {} while trying to update Attribute {} ",
            phoneNumber, attributeName);
        throw new IllegalStateException("No Row found with Phone number " + phoneNumber
            + " while trying to update Attribute " + attributeName);
      }
  
      String requestCellRange = "Requests!" + requestHeaders.getColumnLabel(attributeName) + requestRowNumber;
      logger.info("Updating Cell {} with Value {} for Attribute {} in the Profiles Sheet",
          requestCellRange, phoneNumber, attributeName);
      updateCellValue(cellValue, requestCellRange);
    }
  }
  
  private String getPhoneNumber(ProfileAttribute attribute) {
    ObfuscatedID obfuscatedId = attribute.getIdentityUUID();
    ExternalID number = new IdentityService().lookupNumberById(obfuscatedId);
    String phoneNumber = "";
    if (number != null && number.getExternalId() != null) {
      phoneNumber = number.getExternalId();
    }
    return phoneNumber;
  }
  
  private String getCellValue(ProfileAttribute attribute) {
    if (attribute.getAttributeValue() != null) {
      return attribute.getAttributeValue();
    } else {
      return "";
    }
    // TODO(abhideep): Add support for location and Request Type as well
  }

  private void updateCellValue(String value, String cellRange) throws IOException {
    List<List<Object>> values = new ArrayList<>();
    values.add(Collections.singletonList(value));
  
    ValueRange cell = new ValueRange().setValues(values);
    sheets.spreadsheets().values().update(spreadsheetId, cellRange, cell)
        .setValueInputOption("USER_ENTERED")
        .execute();
  }
  
  private boolean isEarliestAttribute(ProfileAttribute attribute, List<ProfileAttribute> attrList) {
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
