package com.galewings.controller;

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

/**
 * SettingsController
 */
@RequestMapping("/settings")
@Controller
public class SettingsController {

  /**
   * sqlManager
   */
  @Autowired
  SqlManager sqlManager;

  /**
   * 設定ページ初期表示
   *
   * @param model
   * @return 遷移先情報
   * @throws JsonProcessingException
   */
  @GetMapping("")
  @Transactional
  public String index(Model model) throws JsonProcessingException {
    // TODO: リポジトリを使う
    List<Settings> settingsList = sqlManager.getResultList(Settings.class,
        new ClasspathSqlResource("sql/settings/select_setting.sql"));

    model.addAttribute("settings", settingsList);

    return "settings";
  }

  /**
   * 設定情報更新
   *
   * @param form  更新情報
   * @param model
   * @return 遷移先情報
   */
  @PostMapping("/update")
  @Transactional
  public String update(SettingsUpdateForm form, Model model) {
    // TODO: リポジトリを使う
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
