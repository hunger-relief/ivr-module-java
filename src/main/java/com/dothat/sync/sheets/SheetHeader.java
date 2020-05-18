package com.dothat.sync.sheets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class SheetHeader {
  private final List<String> rowValues = new ArrayList<>();
  private final Map<String, Integer> columnIndexMap = new HashMap<>();
  private final Map<String, String> columnNameMap = new HashMap<>();
  
  public SheetHeader(List<Object> headerRowValues) {
    if (headerRowValues != null) {
      int ctr = 1;
      for (Object headerRowValue : headerRowValues) {
        String attributeName = (String) headerRowValue;
        rowValues.add(attributeName);
        columnIndexMap.put(attributeName, ctr);
        ctr++;
      }
    }
  }
  
  public List<String> getRowValues() {
    return rowValues;
  }
  
  public Integer getColumnNumber(String attributeName) {
    return columnIndexMap.get(attributeName);
  }
  
  public String getColumnLabel(String attributeName) {
    Integer columnIndex = getColumnNumber(attributeName);
    if (columnIndex != null) {
      return getColumnLetter(columnIndex);
    }
    return null;
  }
  
  private String getColumnLetter(Integer columnNumber) {
    String columnLabel;
    if (columnNumber < 27) {
      columnLabel = String.valueOf((char)(columnNumber + 64));
    } else {
      int firstChar = columnNumber / 26;
      int secondChar = columnNumber - (firstChar * 26);
      columnLabel = String.valueOf((char)(firstChar + 64)) + String.valueOf((char)(secondChar + 64));
    }
    return columnLabel;
  }
}
