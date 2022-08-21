package com.galewings.repository;

import com.galewings.entity.Category;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CategoryRepository {

  /**
   * SqlManager
   */
  @Autowired
  SqlManager sqlManager;

  @Transactional
  public List<Category> selectAll() {
    return sqlManager.getResultList(Category.class,
        new ClasspathSqlResource("sql/category/select_all_category.sql"));
  }

  @Transactional
  public void add(Category category) {
    category.uuid = UUID.randomUUID().toString();
    sqlManager.insertEntity(category);
  }

  @Transactional
  public void delete(String uuid) {
    Map<String, String> param = new HashMap<>();
    param.put("uuid", uuid);
    sqlManager.executeUpdate(new ClasspathSqlResource("sql/category/delete_category_uuid.sql"),
        param);
  }
}
