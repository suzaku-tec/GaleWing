package com.galewings.repository;

import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@Transactional
public class QiitaTagRepository {

    @Autowired
    private SqlManager sqlManager;

    public boolean isExist(String url, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("url", url);
        params.put("name", name);
        int count = sqlManager.getCount(new ClasspathSqlResource("sql/qiitaTag/select_tag_single.sql"), params);
        return 0 < count;
    }

    public int insert(String url, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("url", url);
        params.put("name", name);
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/qiitaTag/insert_tag.sql"), params);
    }

}
