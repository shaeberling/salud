package com.s13g.salud;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.Collections;
import java.util.List;

public class SheetControllerCreator {

  private static final String APP_NAME = "Salud App";
  /**
   * Global instance of the scopes required by this quickstart.
   * <p>
   * If modifying these scopes, delete your previously saved credentials
   * at ~/.credentials/sheets.googleapis.com-java-quickstart
   */
  private static final List<String> SCOPES =
      Collections.singletonList(SheetsScopes.SPREADSHEETS);


  public static SheetController create() {
    AppIdentityCredential credential = new AppIdentityCredential(SCOPES);
    return new SheetController(
        new Sheets.Builder(new UrlFetchTransport(), new JacksonFactory(), credential)
            .setApplicationName(APP_NAME)
            .build());
  }
}
