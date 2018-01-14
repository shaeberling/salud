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


import com.google.common.base.Strings;
import com.s13g.salud.data.RegisteringServlet;
import com.s13g.salud.ui.JspUtil;
import com.s13g.salud.ui.TemplateModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * A UI to change the settings of the application.
 */
@WebServlet(name = "SettingsServlet", value = "/settings")
public class SettingsServlet extends RegisteringServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    TemplateModel model = new TemplateModel();
    Optional<String> sheetId = SettingsManager.get().getSheetId();
    model.put("sheetId", sheetId.orElse(""));
    JspUtil.writeResponse(getServletContext(), req, resp, "/settings.jsp", model);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String sheetId = req.getParameter("sheetId");
    if (!Strings.isNullOrEmpty(sheetId)) {
      SettingsManager.get().setSheetId(sheetId);
    }
    doGet(req, resp);
  }
}
