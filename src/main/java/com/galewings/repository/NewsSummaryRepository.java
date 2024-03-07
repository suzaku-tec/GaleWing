package com.galewings.repository;

import com.galewings.entity.NewsSummary;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class NewsSummaryRepository {

    @Autowired
    SqlManager sqlManager;

    public void insert(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("feed_uuid", uuid);
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/newsSummary/insert_summary.sql")
                , params);
    }

    public List<NewsSummary> selectAll() {
        return sqlManager.getResultList(NewsSummary.class, new ClasspathSqlResource("sql/newsSummary/select_all.sql"));
    }

    public List<NewsSummary> selectNoSummary() {
        return sqlManager.getResultList(NewsSummary.class, new ClasspathSqlResource("sql/newsSummary/select_no_summary.sql"));
    }

    public NewsSummary select(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        return sqlManager.getSingleResult(NewsSummary.class, new ClasspathSqlResource("sql/newsSummary/select_uuid.sql")
                , params);
    }

    public int update(String uuid, String summary) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        params.put("summary", summary);

        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/newsSummary/update_summary.sql"), params);
    }

    public int delete(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/newsSummary/delete_summary.sql")
                , params);
    }

    public boolean exists(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        return 0 < sqlManager.getCount(new ClasspathSqlResource("sql/newsSummary/select_exists.sql"), params);
    }
}
