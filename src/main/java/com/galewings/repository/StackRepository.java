package com.galewings.repository;

import com.galewings.entity.Stack;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * StackRepository
 */
@Component
public class StackRepository {

  /**
   * SqlManager
   */
  @Autowired
  SqlManager sqlManager;

  /**
   * 全積読情報取得
   *
   * @return 積読情報リスト
   */
  @Transactional
  public List<Stack> getStackList() {
    return sqlManager.getResultList(Stack.class,
        new ClasspathSqlResource("sql/stack/select_all_list.sql"));
  }

  /**
   * 積読情報の追加
   *
   * @param uuid UUID
   * @param link リンク
   * @return 実行結果
   */
  @Transactional
  public int addStack(String uuid, String link) {
    Map<String, String> params = new HashMap<>();
    params.put("uuid", uuid);
    params.put("link", link);
    return sqlManager.executeUpdate(
        new ClasspathSqlResource("sql/stack/insert_stack_from_feed.sql"), params);
  }
}
