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

    private enum FeedType {
        SUMMARY("summary"),
        INFORMATION_GATHERING("information_gathering");

        public final String code;

        FeedType(String code) {
            this.code = code;
        }
    }

    @Autowired
    SqlManager sqlManager;

    public void insertSummary(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("feed_uuid", uuid);
        params.put("feed_type", FeedType.SUMMARY.code);
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/newsSummary/insert_summary.sql")
                , params);
    }

    public void insertInformationGathering(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("feed_uuid", uuid);
        params.put("feed_type", FeedType.INFORMATION_GATHERING.code);
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

    public int updateSummary(String uuid, String summary) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        params.put("summary", summary);
        params.put("type", FeedType.SUMMARY.code);

        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/newsSummary/update_summary.sql"), params);
    }

    public int updateInformationGathering(String uuid, String summary) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        params.put("summary", summary);
        params.put("type", FeedType.INFORMATION_GATHERING.code);

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
