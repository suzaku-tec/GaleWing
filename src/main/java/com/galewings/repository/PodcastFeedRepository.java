package com.galewings.repository;

import com.galewings.entity.PodcastFeed;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PodcastFeedRepository {

    @Autowired
    SqlManager sqlManager;

    @Transactional
    public int insert(PodcastFeed podcastFeed) {
        return sqlManager.insertEntity(podcastFeed);
    }

    @Transactional
    public boolean isExist(String url) {
        Map<String, String> params = new HashMap<>();
        params.put("url", url);

        return 0 < sqlManager.getCount(new ClasspathSqlResource("sql/podcastFeed/selectForUrl.sql"), params);
    }

    @Transactional
    public boolean isNotExist(String url) {
        return !isExist(url);
    }

    @Transactional
    public List<PodcastFeed> selectAll() {
        return sqlManager.getResultList(PodcastFeed.class, new ClasspathSqlResource("sql/podcastFeed/selectAll.sql"));
    }
}
