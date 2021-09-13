package com.galewings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.dto.input.SettingsUpdateForm;
import com.galewings.entity.Settings;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/settings")
@Controller
public class SettingsController {

  @Autowired
  SqlManager sqlManager;

  @GetMapping("")
  @Transactional
  public String index(Model model) throws JsonProcessingException {
    List<Settings> settingsList = sqlManager.getResultList(Settings.class,
        new ClasspathSqlResource("sql/settings/select_setting.sql"));

    model.addAttribute("settings", settingsList);

    return "settings";
  }

  @PostMapping("/update")
  @Transactional
  public String update(SettingsUpdateForm form, Model model) {
    form.getSettings().forEach((key, value) -> {
      Map<String, String> params = new HashMap<>();
      params.put("id", key);
      params.put("setting", value);
      sqlManager.executeUpdate(new ClasspathSqlResource("sql/settings/update_settings.sql"),
          params);
    });

    return "redirect:/settings";
  }
}
