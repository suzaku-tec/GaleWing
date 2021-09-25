package com.galewings.repository;

import com.galewings.entity.Site;
import com.galewings.entity.SiteFeedCount;
import com.galewings.utility.FaviconUtil;
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
public class SiteRepository {

  @Autowired
  SqlManager sqlManager;

  @Autowired
  FaviconUtil faviconUtil;

  public Site getSite(String uuid) {
    Map<String, String> params = new HashMap<>();
    params.put("uuid", uuid);
    Site site = sqlManager.getSingleResult(Site.class,
        new ClasspathSqlResource("sql/select_site_origin.sql"), params);
    return site;
  }

  @Transactional
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

  @Transactional
  public void insertSite(be.ceau.opml.entity.Outline outline) {
    Map<String, String> params = new HashMap<>();
    params.put("htmlUrl", outline.getAttribute("htmlUrl"));
    int count = sqlManager.getCount(
        new ClasspathSqlResource("sql/site/select_site_for_htmlUrl.sql"),
        params
    );

    if (0 < count) {
      return;
    }

    var faviconBase64 = faviconUtil.getBase64Favicon(outline.getAttribute("htmlUrl")).get();

    params = new HashMap<>();
    params.put("uuid", UUID.randomUUID().toString());
    params.put("title", outline.getAttribute("text"));
    params.put("htmlUrl", outline.getAttribute("htmlUrl"));
    params.put("xmlUrl", outline.getAttribute("xmlUrl"));
    params.put("faviconBase64", faviconBase64);

    sqlManager.executeUpdate(new ClasspathSqlResource("sql/site/insert_site.sql"), params);
  }

  @Transactional
  public boolean delete(String uuid) {
    Map<String, String> params = new HashMap<>();
    params.put("uuid", uuid);
    int siteDelCnt = sqlManager.executeUpdate(new ClasspathSqlResource("sql/site/delete.sql"),
        params);

    sqlManager.executeUpdate(new ClasspathSqlResource("sql/feed/delete.sql"),
        params);

    return 0 < siteDelCnt;
  }
}
