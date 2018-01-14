/*
 * Copyright 2018, Sascha Häberling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.s13g.salud;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.s13g.salud.data.Settings;

import java.util.Optional;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Provides access to read and persist global settings.
 */
public class SettingsManager {
  private static final String MEMCACHE_KEY_SHEET_ID = "sheet_id";
  private final MemcacheService memcacheService;
  private String sheetId;

  private static SettingsManager instance;

  /**
   * Get the singleton settings manager.
   */
  static SettingsManager get() {
    if (instance == null) {
      return new SettingsManager(MemcacheServiceFactory.getMemcacheService());
    }
    return instance;
  }

  private SettingsManager(MemcacheService memcacheService) {
    this.memcacheService = memcacheService;
    this.sheetId = null;
  }

  /**
   * Get the sheet ID, if it was persisted.
   */
  Optional<String> getSheetId() {
    if (sheetId != null) {
      return Optional.of(sheetId);
    }

    String id = (String) memcacheService.get(MEMCACHE_KEY_SHEET_ID);
    if (id != null) {
      sheetId = id;
      return Optional.of(id);
    }

    Settings settings = ofy().load().key(Settings.key()).now();
    if (settings == null) {
      return Optional.empty();
    }

    sheetId = settings.sheetId;
    memcacheService.put(MEMCACHE_KEY_SHEET_ID, sheetId);
    return Optional.ofNullable(settings.sheetId);

  }

  /**
   * Sets or updates the sheet ID.
   */
  public void setSheetId(String id) {
    // Update local and memcache values.
    sheetId = id;
    memcacheService.put(MEMCACHE_KEY_SHEET_ID, id);

    // Get and update the persisted data.
    Settings settings = ofy().load().key(Settings.key()).now();
    if (settings == null) {
      settings = new Settings();
    }
    settings.sheetId = id;
    ofy().save().entity(settings).now();
  }
}