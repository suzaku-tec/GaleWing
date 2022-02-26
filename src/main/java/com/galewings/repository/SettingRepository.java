package com.galewings.repository;

import com.galewings.entity.Settings;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
   * @param id    ID
   * @param value 設定値
   * @return 更新件数
   */
  @Transactional
  public int update(String id, String value) {
    Map<String, String> params = new HashMap<>();
    params.put("id", id);
    params.put("setting", value);
    return sqlManager.executeUpdate(new ClasspathSqlResource("sql/settings/update_settings.sql"));
  }

  @Transactional
  public List<Settings> list() {
    return sqlManager.getResultList(Settings.class,
        new ClasspathSqlResource("sql/settings/select_setting.sql"));
  }

}
