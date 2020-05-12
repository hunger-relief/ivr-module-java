package com.dothat.sync.sheets;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class UpdateCellTaskConfig {
  private final String sheetName;
  private final String lookupSheetRange;
  private final String rowColRange;
  
  public UpdateCellTaskConfig(String sheetName, String lookupSheetRange, String rowColRange) {
    this.sheetName = sheetName;
    this.lookupSheetRange = lookupSheetRange;
    this.rowColRange = rowColRange;
  }
  
  public static UpdateCellTaskConfig forRequest() {
    return new UpdateCellTaskConfig("Requests!",
        "Lookup!B3:B5", "Lookup!B6:B7");
  }

  public static UpdateCellTaskConfig forProfile() {
    return new UpdateCellTaskConfig("Profiles!",
        "Lookup!E3:E5", "Lookup!E6:E7");
  }
  
  public String getSheetName() {
    return sheetName;
  }
  
  public String getLookupSheetRange() {
    return lookupSheetRange;
  }
  
  public String getRowColRange() {
    return rowColRange;
  }
}
