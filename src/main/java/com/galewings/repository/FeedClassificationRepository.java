package com.galewings.repository;

import com.galewings.entity.FeedClassification;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
@Component
public class FeedClassificationRepository {

    private final SqlManager sqlManager;

    public FeedClassificationRepository(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public boolean existsClassification(String feedLink) {
        Map<String, String> param = Map.of("feedLink", feedLink);
        Integer count = sqlManager.getSingleResult(Integer.class, new ClasspathSqlResource("sql/feed_classification/select_count_classification.sql"), param);
        return count != null && count > 0;
    }

    public FeedClassification getClassification(String feedLink) {
        Map<String, String> param = Map.of("feedLink", feedLink);
        return sqlManager.getSingleResult(FeedClassification.class, new ClasspathSqlResource("sql/feed_classification/select_classification.sql"), param);
    }

    public int mergeClassification(FeedClassification feedClassification) {
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/feed_classification/insert_feed_classification.sql"), feedClassification);
    }
}
