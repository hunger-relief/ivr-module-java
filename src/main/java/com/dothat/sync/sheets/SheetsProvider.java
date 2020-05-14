package com.dothat.sync.sheets;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.common.collect.Lists;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class SheetsProvider {
  private static final List<String> SCOPES =
    Lists.newArrayList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE_READONLY);

  private static final String CREDENTIALS_FILE_PATH_DEV = "/sync-sheet-hunger-relief-dev.json";
  private static final String CREDENTIALS_FILE_PATH_PROD = "/sync-to-sheet-credentials-hunger-relief-coop.json";
  
  private static final String CREDENTIALS_FILE_PATH = CREDENTIALS_FILE_PATH_PROD;
  public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  
  public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    
    InputStream credentialsFile = SheetsProvider.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (credentialsFile == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
  
    GoogleCredential credential = GoogleCredential
        .fromStream(credentialsFile)
        .createScoped(SCOPES);
    
    return new Sheets.Builder(httpTransport, JSON_FACTORY, credential)
        .setApplicationName("Hunger Relief Dev")
        .build();
  }
}
