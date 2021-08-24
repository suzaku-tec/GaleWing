package com.example.galewings.repository;

import com.example.galewings.entity.Feed;
import com.example.galewings.entity.Site;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeedRepository {

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

    public List<Feed> getAllFeed() {
        return sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/feed/select_all_feed.sql"));
    }

    public List<Feed> getSiteFeed(Site site) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", site.uuid);
        return sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/feed/select_site_feed.sql"), params);
    }
}
