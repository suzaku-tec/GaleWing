package com.galewings.repository;

import com.galewings.entity.Settings;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingRepository {

  /**
   * SqlManager
   */
  @Autowired
  SqlManager sqlManager;

  public int update(String id, String value) {
    Map<String, String> params = new HashMap<>();
    params.put("id", id);
    params.put("setting", value);
    return sqlManager.executeUpdate(new ClasspathSqlResource("sql/settings/update_settings.sql"),
        params);

  }

  /**
   * 設定情報取得
   *
   * @return 設定情報リスト
   */
  public List<Settings> getSettingAllList() {
    return sqlManager.getResultList(Settings.class,
        new ClasspathSqlResource("sql/settings/select_setting.sql"));
  }

}
