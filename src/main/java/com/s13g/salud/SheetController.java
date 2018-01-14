/*
 * Copyright 2017, Sascha Häberling
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

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Communicates with Google Sheets add new values.
 */
public class SheetController {
  private static final String RANGE = "Data Entry!A:E";

  private static final Map<EntryType, Integer> ASSIGNMENTS = getAssignments();

  private final String sheetId;
  final Sheets sheets;

  SheetController(String sheetId, Sheets sheets) {
    this.sheetId = sheetId;
    this.sheets = sheets;
  }

  public boolean addEntry(EntryType type, int value) throws IOException {
    ValueRange values = valuesFromRequest(type, value);
    AppendValuesResponse response = sheets.spreadsheets().values()
        .append(sheetId, RANGE, values)
        .setValueInputOption("USER_ENTERED")
        .setInsertDataOption("INSERT_ROWS")
        .execute();
    return response.getUpdates().getUpdatedRows() == 1;
  }

  private ValueRange valuesFromRequest(EntryType type, int value) throws IOException {
    if (!ASSIGNMENTS.containsKey(EntryType.DATE)) {
      throw new IOException("Internal error: Assignments has no date entry");
    }
    if (!ASSIGNMENTS.containsKey(type)) {
      throw new IOException("Internal error: Assignments has no entry for " + type);
    }

    int largestIndex = ASSIGNMENTS.values().stream().mapToInt(o -> o).max().getAsInt();
    List<List<Object>> values = new ArrayList<>();
    List<Object> row = new ArrayList<>();

    for (int i = 0; i <= largestIndex; ++i) {
      row.add("");
    }

    row.add(ASSIGNMENTS.get(EntryType.DATE), getNow());
    row.add(ASSIGNMENTS.get(type), value);

    values.add(row);
    return new ValueRange().setValues(values);
  }

  private String getNow() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    return sdf.format(new Date());
  }

  private static Map<EntryType, Integer> getAssignments() {
    Map<EntryType, Integer> assignments = new HashMap<>();
    assignments.put(EntryType.DATE, 0);
    assignments.put(EntryType.CARDIO_CALORIES, 1);
    assignments.put(EntryType.PUSHUPS, 2);
    assignments.put(EntryType.SITUPS, 3);
    assignments.put(EntryType.SQUADS, 4);
    return assignments;
  }
}
