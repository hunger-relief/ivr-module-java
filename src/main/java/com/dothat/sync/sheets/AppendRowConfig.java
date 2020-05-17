package com.dothat.sync.sheets;

/**
 * Configuration for Appending Rows to a Sheet.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class AppendRowConfig {
  private final String sheetName;
  private final String lookupSheetRange;
  
  public AppendRowConfig(String sheetName, String lookupSheetRange) {
    this.sheetName = sheetName;
    this.lookupSheetRange = lookupSheetRange;
  }
  
  public static AppendRowConfig forRequest() {
    return new AppendRowConfig("Requests!", "A1:Z");
  }

  public static AppendRowConfig forProfile() {
    return new AppendRowConfig("Profiles!", "A1:Z");
  }
  
  public String getSheetName() {
    return sheetName;
  }
  
  public String getLookupSheetRange() {
    return lookupSheetRange;
  }
}
