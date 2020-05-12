package com.dothat.sync.archive;

/**
 * @author abhideep@ (Abhideep Singh)
 */
public class GoogleSheetsCredentials {
/**
  public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    // TODO: Change placeholder below to generate authentication credentials. See
    // https://developers.google.com/sheets/quickstart/java#step_3_set_up_the_sample
    //
    // Authorize using one of the following scopes:
    //   "https://www.googleapis.com/auth/drive"
    //   "https://www.googleapis.com/auth/drive.file"
    //   "https://www.googleapis.com/auth/drive.readonly"
    //   "https://www.googleapis.com/auth/spreadsheets"
    //   "https://www.googleapis.com/auth/spreadsheets.readonly"
    GoogleCredential credential = createCredentialForServiceAccount(httpTransport, jsonFactory,
        "",  Collections.singleton(SheetsScopes.SPREADSHEETS), null);
    
    return new Sheets.Builder(httpTransport, jsonFactory, credential)
        .setApplicationName("Google-SheetsSample/0.1")
        .build();
  }
  
  public static GoogleCredentials getDefaultCredentials() throws IOException {
    return AppEngineCredentials.getApplicationDefault();
  }
  
  public static GoogleCredential createCredentialForServiceAccount(
      HttpTransport transport,
      JsonFactory jsonFactory,
      String serviceAccountId,
      Collection<String> serviceAccountScopes,
      File p12File) throws GeneralSecurityException, IOException {
    return new GoogleCredential.Builder().setTransport(transport)
        .setJsonFactory(jsonFactory)
        .setServiceAccountId(serviceAccountId)
        .setServiceAccountScopes(serviceAccountScopes)
        .setServiceAccountPrivateKeyFromP12File(p12File)
        .build();
  }

  public static GoogleCredentials getCredentialsFromJson(String jsonPath) throws IOException {
    return GoogleCredentials
        .fromStream(new FileInputStream(jsonPath))
        .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
  }
  
  public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
    Sheets sheets = new Sheets.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        JacksonFactory.getDefaultInstance(),
        getCredentialsFromJson(""))
        .setApplicationName("Google Sheets Client").build();
    return sheets;
  }
 */
}