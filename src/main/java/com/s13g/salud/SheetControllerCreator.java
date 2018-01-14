package com.s13g.salud;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.common.base.Strings;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class SheetControllerCreator {
  private static final String APP_NAME = "Salud App";
  /**
   * Global instance of the scopes required by this quickstart.
   * <p>
   * If modifying these scopes, delete your previously saved credentials
   * at ~/.credentials/sheets.googleapis.com-java-quickstart
   */
  private static final List<String> SCOPES =
      Collections.singletonList(SheetsScopes.SPREADSHEETS);

  static SheetController create() throws InstantiationException {
    SettingsManager settingsManager = SettingsManager.get();
    Optional<String> sheetId = settingsManager.getSheetId();
    if (!sheetId.isPresent() || Strings.isNullOrEmpty(sheetId.get())) {
      throw new InstantiationException("Sheet ID not present. Please set first!");
    }
    AppIdentityCredential credential = new AppIdentityCredential(SCOPES);
    return new SheetController(sheetId.get(),
        new Sheets.Builder(new UrlFetchTransport(), new JacksonFactory(), credential)
            .setApplicationName(APP_NAME)
            .build());
  }
}
