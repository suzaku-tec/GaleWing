package com.galewings.repository;

import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TrendRepository {

    @Autowired
    private SqlManager sqlManager;

    public int insert(String date, String word, Long count) {
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("word", word);
        map.put("count", count);

        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/trend/insert.sql"), map);
    }
}
