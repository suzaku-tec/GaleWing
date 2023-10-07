package com.galewings.repository;

import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@Transactional
public class WordRepository {

    @Autowired
    private SqlManager sqlManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isExist(String word) {
        Map<String, String> params = new HashMap<>();
        params.put("word", word);
        Integer count = sqlManager.getSingleResult(Integer.class, new ClasspathSqlResource("sql/word/select_ " +
                "count_word.sql"), params);

        return 0 == count;
    }

    public int insert(String word) {
        Map<String, String> params = new HashMap<>();
        params.put("word", word);
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/word/insert_word.sql"), params);
    }

}
