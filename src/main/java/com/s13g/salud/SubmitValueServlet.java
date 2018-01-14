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

import com.google.common.base.Strings;
import com.s13g.salud.data.RegisteringServlet;
import com.s13g.salud.ui.JspUtil;
import com.s13g.salud.ui.TemplateModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.s13g.salud.EntryType.*;

@WebServlet(name = "SubmitValueServlet", value = "/submit")
public class SubmitValueServlet extends RegisteringServlet {
  private static final Logger LOG = Logger.getLogger("SubmitValue");

  private static final String PARAM_CARDIO_CALORIES = "cardio_calories";
  private static final String PARAM_PUSHUPS = "pushups";
  private static final String PARAM_SITUPS = "situps";
  private static final String PARAM_SQUADS = "squads";

  private static final String MSG_SUCCESS = "\nOK: Entry '%s' successfully submitted as %d.";
  private static final String MSG_FAILURE = "\nFAIL: Something went wrong: %s";

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    List<ExerciseEntry> entries = getEntries();
    TemplateModel model = new TemplateModel();
    StringBuilder message = new StringBuilder();
    // The way the forms are laid out right now, only one value can be submitted at a time.
    for (ExerciseEntry entry : entries) {
      String valueStr = req.getParameter(entry.paramName);
      if (Strings.isNullOrEmpty(valueStr)) {
        continue;
      }
      try {
        // TODO: If we support weight, we might want to support floats here.
        message.append(handleSubmission(entry, Integer.parseInt(valueStr)));
      } catch (NumberFormatException ex) {
        message.append(String.format(MSG_FAILURE, "Cannot parse number " + valueStr));
      } catch (InstantiationException e) {
        message.append(String.format(MSG_FAILURE, e.getMessage()));
      }
    }
    model.put("message", message.toString());
    JspUtil.writeResponse(getServletContext(), req, resp, "/submit.jsp", model);
  }

  private String handleSubmission(ExerciseEntry entry, int value) throws IOException,
      InstantiationException {
    StringBuilder message = new StringBuilder();
    SheetController sheetController = SheetControllerCreator.create();
    if (!sheetController.addEntry(entry.type, value)) {
      message.append(String.format(MSG_FAILURE, "Error while updating spreadsheet."));
    } else {
      message.append(String.format(MSG_SUCCESS, entry.label, value));
    }
    return message.toString();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    TemplateModel model = new TemplateModel();
    model.put("message", "Holaaaa. Como estás?");
    JspUtil.writeResponse(getServletContext(), req, resp, "/submit.jsp", model);
  }

  @SuppressWarnings("WeakerAccess") // Called from JSP.
  public static List<ExerciseEntry> getEntries() {
    List<ExerciseEntry> result = new ArrayList<>();
    result.add(new ExerciseEntry(PARAM_CARDIO_CALORIES, "Cardio Calories", CARDIO_CALORIES));
    result.add(new ExerciseEntry(PARAM_PUSHUPS, "# of pushups", PUSHUPS));
    result.add(new ExerciseEntry(PARAM_SITUPS, "# of situps", SITUPS));
    result.add(new ExerciseEntry(PARAM_SQUADS, "# of squads", SQUADS));
    return result;
  }

  /**
   * This is one exercise entry that is listed on the page and that we can collect data for.
   */
  public static final class ExerciseEntry {
    /**
     * The HTTP parameter name for this entry.
     */
    final String paramName;
    /**
     * The human-readable label for this entry.
     */
    final String label;

    final EntryType type;

    ExerciseEntry(String paramName, String label, EntryType type) {
      this.paramName = paramName;
      this.label = label;
      this.type = type;
    }

    public String getLabel() {
      return label;
    }

    public String getParamName() {
      return paramName;
    }
  }

}
