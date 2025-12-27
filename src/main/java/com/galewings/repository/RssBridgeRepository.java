package com.galewings.repository;

import com.galewings.dto.RssBridgeKey;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RssBridgeRepository {

    private final SqlManager sqlManager;

    @Autowired
    public RssBridgeRepository(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public int insert(String id, String json, String title) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("json", json);
        map.put("title", title);
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/rssBridge/insert.sql"), map);

    }

    public int isExists(String jsonId) {
        Map map = new HashMap();
        map.put("jsonId", jsonId);
        return sqlManager.getCount(new ClasspathSqlResource("sql/rssBridge/isExists.sql"), map);
    }

    public List<String> getJsonList(String title) {
        Map map = new HashMap();
        map.put("title", title);
        return sqlManager.getResultList(String.class, new ClasspathSqlResource("sql/rssBridge/select_content_html.sql"), map);
    }

    public List<String> selectIdList() {
        return sqlManager.getResultList(String.class, new ClasspathSqlResource("sql/rssBridge/select_id_list.sql"));
    }

    public List<RssBridgeKey> selectKeyList() {
        return sqlManager.getResultList(RssBridgeKey.class, new ClasspathSqlResource("sql/rssBridge/select_key_list.sql"));
    }
}
