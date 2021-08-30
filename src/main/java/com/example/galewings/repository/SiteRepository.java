package com.example.galewings.repository;

import com.example.galewings.entity.Site;
import com.example.galewings.entity.SiteFeedCount;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SiteRepository {

  @Autowired
  SqlManager sqlManager;

  public Site getSite(String uuid) {
    Map<String, String> params = new HashMap<>();
    params.put("uuid", uuid);
    Site site = sqlManager.getSingleResult(Site.class,
        new ClasspathSqlResource("sql/select_site_origin.sql"), params);
    return site;
  }

  public List<Site> getAllSite() {
    return sqlManager.getResultList(Site.class, new ClasspathSqlResource(
        "sql/site/select_all_site.sql"));
  }

  public List<SiteFeedCount> getSiteFeedCount() {
    return sqlManager.getResultList(SiteFeedCount.class,
        new ClasspathSqlResource("sql/site/select_all_site_count_feed.sql"));
  }

  public List<SiteFeedCount> getSiteFeedCount(String link) {
    Map<String, String> params = new HashMap<>();
    params.put("link", link);

    return sqlManager.getResultList(SiteFeedCount.class,
        new ClasspathSqlResource("sql/site/select_site_count_for_link.sql"), params);
  }
}
