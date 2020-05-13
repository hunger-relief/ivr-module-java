package com.dothat.sync.sheets;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class AppendRowConfig {
  private final String sheetName;
  private final String lookupSheetRange;
  private final String startColumn;
  private final String endColumn;
  
  public AppendRowConfig(String sheetName, String lookupSheetRange, String startColumn, String endColumn) {
    this.sheetName = sheetName;
    this.lookupSheetRange = lookupSheetRange;
    this.startColumn = startColumn;
    this.endColumn = endColumn;
  }
  
  public static AppendRowConfig forRequest() {
    return new AppendRowConfig("Requests!",
        "A1:F", "A", "Z");
  }

  public static AppendRowConfig forProfile() {
    return new AppendRowConfig("Profiles!",
        "A1:G", "A", "G");
  }
  
  public String getSheetName() {
    return sheetName;
  }
  
  public String getLookupSheetRange() {
    return lookupSheetRange;
  }
  
  public String getStartColumn() {
    return startColumn;
  }
  
  public String getEndColumn() {
    return endColumn;
  }
}
