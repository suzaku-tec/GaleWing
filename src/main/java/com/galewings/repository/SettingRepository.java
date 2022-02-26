package com.galewings.repository;

import com.galewings.dto.input.SettingsUpdateForm;
import com.galewings.entity.Settings;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Component
public class SettingRepository {

  /**
   * sqlManager
   */
  @Autowired
  SqlManager sqlManager;

  /**
   * update
   *
   * @param form
   * @param model
   * @return
   */
  @Transactional
  public int update(SettingsUpdateForm form, Model model) {

    return form.getSettings().entrySet().stream().map(
        e -> {
          Map<String, String> params = new HashMap<>();
          params.put("id", e.getKey());
          params.put("setting", e.getValue());
          return params;
        }
    ).mapToInt(params -> {
      return sqlManager.executeUpdate(new ClasspathSqlResource("sql/settings/update_settings.sql"),
          params);
    }).sum();
  }

  @Transactional
  public List<Settings> list() {
    return sqlManager.getResultList(Settings.class,
        new ClasspathSqlResource("sql/settings/select_setting.sql"));
  }

}
