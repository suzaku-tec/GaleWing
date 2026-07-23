package com.galewings.repository;

import com.galewings.entity.AIRecommendFeed;
import com.galewings.entity.Feed;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AIRecommendRepository {

    private final SqlManager sqlManager;

    public AIRecommendRepository(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public int insertAIRecommend(String id, String title) {
        Map<String, Object> params = Map.of(
                "id", id,
                "title", title
        );

        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/ai_recommend/insert_ai_recommend_title.sql"), params);
    }

    public int insertAIRecommendOrigin(String id, String url) {
        Map<String, String> params = Map.of(
                "id", id,
                "url", url
        );

        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/ai_recommend/insert_ai_recommend_origin.sql"), params);
    }

    public List<AIRecommendFeed> selectAIRecommndFeed() {
        return sqlManager.getResultList(AIRecommendFeed.class, new ClasspathSqlResource("sql/ai_recommend/select_ai_recommend_feed.sql"));
    }

    public List<Feed> selectAIRecommendOrigin(String id) {
        Map<String, Object> params = Map.of("id", id);
        return sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/ai_recommend/select_ai_recommend_origin.sql"), params);
    }

    public int deleteAIRecommendOrigin(Map<String, Object> params) {
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/ai_recommend/delete_ai_recommend_origin.sql"), params);
    }

    public int deleteAIRecommendFeed(Map<String, Object> params) {
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/ai_recommend/delete_ai_recommend_feed.sql"), params);
    }
}
