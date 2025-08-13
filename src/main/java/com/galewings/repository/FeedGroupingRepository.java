package com.galewings.repository;

import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FeedGroupingRepository {

    private final SqlManager sqlManager;

    @Autowired
    public FeedGroupingRepository(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public void insert(String feedUuid1, String feedUuid2) {
        Map<String, String> param = Map.of(
                "feedUuid1", feedUuid1,
                "feedUuid2", feedUuid2
        );
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/feedGrouping/insert.sql"),
                param);
    }
}
