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

package com.s13g.salud.data;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Data type for settings of the application to be persisted.
 */
@Entity
public class Settings {

  private static final long SINGLETON_ID = 42;

  @Id
  Long id;

  /**
   * The Google Sheet ID to use for adding the data.
   */
  public String sheetId;

  /**
   * Create a  key for the Settings with the given ID.
   */
  public static Key<Settings> key() {
    // We only want one settings item in the data store.
    return Key.create(Settings.class, SINGLETON_ID);
  }

  public Settings() {
    // We only want one settings item in the data store.
    id = SINGLETON_ID;
  }


}
