package com.galewings.repository;

import com.galewings.entity.Podcast;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PodcastRepository {

    @Autowired
    SqlManager sqlManager;

    public List<Podcast> selectAll() {
        return sqlManager.getResultList(Podcast.class,
                new ClasspathSqlResource("sql/podcast/selectAll.sql"));
    }

    public int insert(Podcast podcast) {
        return sqlManager.insertEntity(podcast);
    }
}
