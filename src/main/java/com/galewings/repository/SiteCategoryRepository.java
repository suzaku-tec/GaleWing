package com.galewings.repository;

import com.galewings.entity.Category;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SiteCategoryRepository {

  @Autowired
  SqlManager sqlManager;

  @Transactional
  public List<Category> selectCategoryFor(String siteUuid) {
    Map<String, String> param = new HashMap<>();
    param.put("siteUuid", siteUuid);

    return sqlManager.getResultList(Category.class,
        new ClasspathSqlResource("sql/siteCategory/select_category_for_site.sql"), param);
  }

  @Transactional
  public int add(String categoryUuid, String siteUuid) {
    Map<String, String> param = new HashMap<>();
    param.put("categoryUuid", categoryUuid);
    param.put("siteUuid", siteUuid);
    return sqlManager.executeUpdate(
        new ClasspathSqlResource("sql/siteCategory/insert_site_category.sql"), param);
  }

  @Transactional
  public int deleteCategory(String categoryUuid, String siteUuid) {
    Map<String, String> param = new HashMap<>();
    param.put("categoryUuid", categoryUuid);
    param.put("siteUuid", siteUuid);
    return sqlManager.executeUpdate(
        new ClasspathSqlResource("sql/siteCategory/delete_site_category.sql"), param);
  }
}
