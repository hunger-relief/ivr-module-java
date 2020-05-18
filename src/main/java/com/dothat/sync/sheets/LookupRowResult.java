package com.dothat.sync.sheets;

import com.dothat.common.objectify.JodaUtils;
import com.dothat.profile.ProfileField;
import com.dothat.relief.request.data.SourceType;
import com.dothat.relief.request.field.RequestField;
import com.google.api.server.spi.types.DateAndTime;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Columns that provide a mechanism(key) to lookup Sheet Rows By.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class LookupRowResult {
  private static final Logger logger = LoggerFactory.getLogger(LookupRowResult.class);

  private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyyMMdd");
  final static public LocalDate EXCEL_EPOCH_REFERENCE = new LocalDate(1899 , 12 , 30 );

  private final List<List<List<Object>>> columnValuesList;
  private final List<String> fieldNames;
  private final int maxRows;
  
  LookupRowResult(List<List<List<Object>>> columnValuesList, List<String> fieldNames) {
    this.columnValuesList = columnValuesList;
    this.fieldNames = fieldNames;
    this.maxRows = getMaxRows();
  }
  
  public Integer getRowForSource(SourceType sourceType, String source, String sourceId) {
    StringBuilder keyBuilder = new StringBuilder();
    for (String fieldName : fieldNames) {
      if (fieldName.equals(ProfileField.SOURCE_TYPE.name())) {
        keyBuilder.append(sourceType == null ? "" : sourceType.name()).append(":");
      } else if (fieldName.equals(ProfileField.SOURCE.name())) {
        keyBuilder.append(source == null ? "" : source).append(":");
      } else if (fieldName.equals(ProfileField.SOURCE_ID.name())) {
        keyBuilder.append(sourceId == null ? "" : sourceId).append(":");
      }
    }
    String key = keyBuilder.toString();
    logger.info("Searching for Row with key {} ", key);

    for (int rowNumber = maxRows; rowNumber > 0; rowNumber--) {
      String rowKey = getRowKey(rowNumber);
      logger.debug("Comparing Row key {} for Row {} with Search Key {}", rowKey, rowNumber, key);
      if (rowKey.equals(key)) {
        return rowNumber;
      }
    }
    return null;
  }
  
  public Integer getLastRowForPhone(String phoneNumber, DateAndTime timestamp) {
    String phoneKey = phoneNumber + ":";

    DateTime dateTime = JodaUtils.toDateTime(timestamp);
    String phoneDateKey = phoneKey + getDateValue(dateTime) + ":";
    
    logger.info("Searching for Row with key {} ", phoneDateKey);
    for (int rowNumber = maxRows; rowNumber > 0; rowNumber--) {
      String rowKey = getRowKey(rowNumber);
      logger.debug("Comparing Row key {} for Row {} with Search Key {}", rowKey, rowNumber, phoneDateKey);
      if (rowKey.equals(phoneDateKey)) {
        return rowNumber;
      }
    }
  
    logger.info("Searching for Alternate Row with key {} ", phoneKey);
    List<String> onlyPhoneField = Collections.singletonList(RequestField.REQUEST_DATE.name());
    for (int rowNumber = maxRows; rowNumber > 0; rowNumber--) {
      String rowKey = getRowKey(rowNumber, onlyPhoneField);
      logger.debug("Comparing Row key {} for Row {} with Search Key {}", rowKey, rowNumber, phoneKey);
      if (rowKey.equals(phoneKey)) {
        return rowNumber;
      }
    }
    return null;
  }
  
  private String getRowKey(int rowNumber) {
    return getRowKey(rowNumber, fieldNames);
  }

  private String getRowKey(int rowNumber, List<String> fieldNameList) {
    StringBuilder keyBuilder = new StringBuilder();
    for (int fieldIndex = 0; fieldIndex < fieldNameList.size(); fieldIndex++) {
      String value = "";
      List<List<Object>> fieldColumnValues = columnValuesList.get(fieldIndex);
      if (fieldColumnValues.size() >= rowNumber) {
        List<Object> rowValues = fieldColumnValues.get(rowNumber - 1);
        if (rowValues != null) {
          if (rowValues.size() > 0 && rowValues.get(0) != null) {
            Object cellValue = rowValues.get(0);
            if (cellValue instanceof String) {
              value = (String) rowValues.get(0);
            } else if (cellValue instanceof BigDecimal) {
              BigDecimal dateEpoch = (BigDecimal) rowValues.get(0);
              value = convertDate(dateEpoch);
            }
          }
        }
      }
      keyBuilder.append(value).append(":");
    }
    return keyBuilder.toString();
  }

  private String convertDate(BigDecimal countFromEpoch) {
    int days = countFromEpoch.intValue();
    LocalDate localDate = EXCEL_EPOCH_REFERENCE.plusDays(days);
    String value = FORMATTER.print(localDate);
    logger.debug("Convert Value " + countFromEpoch + " into " + value);
    return value;
  }
  
  private String getDateValue(DateTime dateTime) {
    return FORMATTER.print(dateTime);
  }
  
  private int getMaxRows() {
    int maxValues = 0;
    for (List<List<Object>> columnValues : columnValuesList) {
      if (columnValues.size() > maxValues) {
        maxValues = columnValues.size();
      }
    }
    return maxValues;
  }
}
