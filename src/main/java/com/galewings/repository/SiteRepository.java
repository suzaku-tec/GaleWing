package com.galewings.repository;

import com.galewings.entity.Site;
import com.galewings.entity.SiteFeedCount;
import com.galewings.service.FaviconService;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SiteRepository
 */
@Component
public class SiteRepository {

  /**
   * SqlManager
   */
  @Autowired
  SqlManager sqlManager;

  /**
   * FaviconService
   */
  @Autowired
  FaviconService faviconUtil;

  /**
   * サイト情報取得
   *
   * @param uuid UUID
   * @return サイト情報
   */
  @Transactional
  public Site getSite(String uuid) {
    Map<String, String> params = new HashMap<>();
    params.put("uuid", uuid);
    Site site = sqlManager.getSingleResult(Site.class,
        new ClasspathSqlResource("sql/select_site_origin.sql"), params);
    return site;
  }

  /**
   * 全サイト情報取得
   *
   * @return 全サイト情報リスト
   */
  @Transactional
  public List<Site> getAllSite() {
    return sqlManager.getResultList(Site.class, new ClasspathSqlResource(
        "sql/site/select_all_site.sql"));
  }

  /**
   * サイト別フィード件数取得
   *
   * @return サイト別フィード件数リスト
   */
  @Transactional
  public List<SiteFeedCount> getSiteFeedCount() {
    return sqlManager.getResultList(SiteFeedCount.class,
        new ClasspathSqlResource("sql/site/select_all_site_count_feed.sql"));
  }

  /**
   * 特定サイトのフィード件数取得
   *
   * @param link リンク
   * @return フィード件数
   */
  @Transactional
  public List<SiteFeedCount> getSiteFeedCount(String link) {
    Map<String, String> params = new HashMap<>();
    params.put("link", link);

    return sqlManager.getResultList(SiteFeedCount.class,
        new ClasspathSqlResource("sql/site/select_site_count_for_link.sql"), params);
  }

  /**
   * サイト情報追加
   *
   * @param outline
   */
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

    var faviconBase64 = faviconUtil.getBase64Favicon(outline.getAttribute("htmlUrl")).orElse(null);

    params = new HashMap<>();
    params.put("uuid", UUID.randomUUID().toString());
    params.put("title", outline.getAttribute("text"));
    params.put("htmlUrl", outline.getAttribute("htmlUrl"));
    params.put("xmlUrl", outline.getAttribute("xmlUrl"));
    params.put("faviconBase64", faviconBase64);

    sqlManager.executeUpdate(new ClasspathSqlResource("sql/site/insert_site.sql"), params);
  }

  /**
   * サイト情報削除
   *
   * @param uuid UUID
   * @return 実行結果
   */
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

  /**
   * サイト情報追加
   *
   * @param site サイト情報
   * @return
   */
  @Transactional
  public int insertEntity(Site site) {
    return sqlManager.insertEntity(site);
  }

}
