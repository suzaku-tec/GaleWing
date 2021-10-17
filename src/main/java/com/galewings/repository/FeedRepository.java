package com.galewings.repository;

import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
  public int updateReadFeed(String link) {
    Map<String, String> params = new HashMap<>();
    params.put("link", link);
    return sqlManager.executeUpdate(
        new ClasspathSqlResource("sql/feed/update_feed_readed.sql")
        , params
    );
  }

  /**
   * フィード登録
   *
   * @param feed
   * @return
   */
  public int insertEntity(Feed feed) {
    return sqlManager.insertEntity(feed);
  }

}
