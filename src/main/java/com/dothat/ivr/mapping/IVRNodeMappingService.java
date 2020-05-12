package com.dothat.ivr.mapping;

import com.dothat.ivr.notif.data.IVRProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Service Layer to manage IVR Node to Attribute Mapping.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class IVRNodeMappingService {
  public static final String GROUP_SIZE = "GROUP_SIZE";
  public static final String GROUP_WOMEN_CHILD_COUNT = "GROUP_WOMEN_CHILD_COUNT";
  public static final String RESERVES_RATION = "RESERVES_RATION";
  public static final String RESERVES_MONEY = "RESERVES_MONEY";
  public static final String INCOME_TYPE = "INCOME_TYPE";
  public static final String INCOME_DAILY = "INCOME_DAILY";
  public static final String INCOME_MONTHLY = "INCOME_MONTHLY";
  public static final String ACCOMMODATION_TYPE = "ACCOMMODATION_TYPE";
  
  private static Map<String, Map<String, String>> valueMap = new HashMap<>();
  
  static {
    initValueMapping();
  }

  public IVRNodeMappingService() {
    // Nothing for now.
  }
  
  private static void initValueMapping() {
    Map<String, String> rationMap = new HashMap<>();
    rationMap.put("1", "No Ration Left");
    rationMap.put("2", "1 to 3 Days");
    rationMap.put("3", "3 to 7 Days");
    rationMap.put("4", "More than 7 Days");
    valueMap.put(RESERVES_RATION, rationMap);
    
    Map<String, String> moneyMap = new HashMap<>();
    moneyMap.put("1", "Less than 500");
    moneyMap.put("2", "500 to 2000");
    moneyMap.put("3", "2000 to 5000");
    moneyMap.put("4", "More than 5000");
    valueMap.put(RESERVES_MONEY, moneyMap);
  
    Map<String, String> incomeMap = new HashMap<>();
    incomeMap.put("1", "Daily Wages");
    incomeMap.put("2", "Monthly Salary");
    valueMap.put(INCOME_TYPE, incomeMap);
  
    Map<String, String> wageMap = new HashMap<>();
    wageMap.put("1", "Less than 300");
    wageMap.put("2", "300 to 400");
    wageMap.put("3", "400 to 600");
    wageMap.put("4", "More than 600");
    valueMap.put(INCOME_DAILY, wageMap);
  
    Map<String, String> salaryMap = new HashMap<>();
    salaryMap.put("1", "Less than 6000");
    salaryMap.put("2", "6000 to 9000");
    salaryMap.put("3", "9000 to 12000");
    salaryMap.put("4", "More than 12000");
    valueMap.put(INCOME_MONTHLY, salaryMap);
    
    Map<String, String> accommodationMap = new HashMap<>();
    accommodationMap.put("1", "Jhuggi");
    accommodationMap.put("2", "House");
    accommodationMap.put("3", "Labour Camp");
    accommodationMap.put("4", "Factory / Shop");
    accommodationMap.put("5", "Relief Center");
    accommodationMap.put("6", "Other");
    valueMap.put(ACCOMMODATION_TYPE, accommodationMap);
  }
  
  public String getAttributeName(String providerNodeId, IVRProvider provider) {
    // Store configuration in the database.
    return providerNodeId;
  }
  
  public String getAttributeValue(String keypress, String providerNodeId, IVRProvider provider) {
    if (valueMap.containsKey(providerNodeId)) {
      return valueMap.get(providerNodeId).get(keypress);
    } else {
      return keypress;
    }
  }
}
