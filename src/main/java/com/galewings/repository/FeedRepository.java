package com.galewings.repository;

import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.google.common.base.Strings;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * FeedRepository
 */
@Component
public class FeedRepository {

  /**
   * SqlManager
   */
  @Autowired
  SqlManager sqlManager;

  /**
   * フィードの存在確認
   *
   * @param link リンクURL
   * @return true:存在 false:未存在
   */
  @Transactional
  public boolean existFeed(String link) {
    Map<String, String> params = new HashMap<>();
    params.put("link", link);
    int count = sqlManager.getCount(new ClasspathSqlResource("sql/select_feed_single.sql"), params);
    return 0 < count;
  }

  /**
   * 全フィード取得
   *
   * @return フィードリスト
   */
  @Transactional
  public List<Feed> getAllFeed() {
    return sqlManager.getResultList(Feed.class,
        new ClasspathSqlResource("sql/feed/select_all_feed.sql"));
  }

  /**
   * サイトの全フィード取得
   *
   * @param site サイト情報
   * @return サイトの全フィード
   */
  @Transactional
  public List<Feed> getSiteFeed(Site site) {
    Map<String, String> params = new HashMap<>();
    params.put("uuid", site.uuid);
    return sqlManager.getResultList(Feed.class,
        new ClasspathSqlResource("sql/feed/select_site_feed.sql"), params);
  }

  /**
   * サイトフィード既読処理
   *
   * @param identifier サイト識別情報
   * @return 件数
   */
  @Transactional
  public int updateSiteFeedRead(String identifier) {
    Map<String, String> params = new HashMap<>();
    params.put("identifier", identifier);

    return sqlManager.executeUpdate(new ClasspathSqlResource("sql/feed/update_site_feed_read.sql"),
        params);
  }

  /**
   * フィード取得
   *
   * @param uuid UUID
   * @return フィードリスト
   */
  @Transactional
  public List<Feed> getFeed(String uuid) {
    Map<String, String> params = new HashMap<>();
    params.put("uuid", uuid);
    return sqlManager.getResultList(Feed.class,
        new ClasspathSqlResource("sql/feed/select_feed_for_uuid.sql"), params);
  }

  /**
   * 既読
   *
   * @param link リンク
   * @return 更新件数
   */
  @Transactional
  public int updateReadFeed(String link) {
    Map<String, String> params = new HashMap<>();
    params.put("link", link);
    return sqlManager.executeUpdate(
        new ClasspathSqlResource("sql/feed/update_feed_read.sql")
        , params
    );
  }

  /**
   * フィード登録
   *
   * @param feed
   * @return
   */
  @Transactional
  public int insertEntity(Feed feed) {
    return sqlManager.insertEntity(feed);
  }

  /**
   * 指定した日付以降のデータを取得する
   *
   * @param date 指定日
   * @return フィードリスト
   */
  @Transactional
  public List<Feed> selectPublicDateFrom(Date date) {
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Map<String, String> params = new HashMap<>();
    params.put("fromDate", sdFormat.format(date));

    return sqlManager.getResultList(Feed.class,
        new ClasspathSqlResource("sql/feed/select_public_from.sql"), params);
  }

  /**
   * フィード単一取得
   *
   * @param link 対象リンク
   * @return フィード
   */
  @Transactional
  public Feed selectFeedFor(String link) {
    Map<String, String> params = new HashMap<>();
    params.put("link", link);
    return sqlManager.getSingleResult(Feed.class,
        new ClasspathSqlResource("sql/feed/select_for_link.sql"),
        params);
  }

  @Transactional
  public void deleteReadFeed(String daysRetained) {

    LocalDate now = LocalDate.now();
    now.minusDays(Strings.isNullOrEmpty(daysRetained) ? 0 : Integer.parseInt(daysRetained));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Map<String, String> params = new HashMap<>();
    params.put("daysRetained", now.format(formatter));
    sqlManager.executeUpdate(
        new ClasspathSqlResource("sql/feed/delete_read_feed.sql"), params
    );
  }
}
