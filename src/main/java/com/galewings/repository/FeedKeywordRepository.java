package com.galewings.repository;

import com.galewings.dto.output.KeywordPrevDayComparisonDto;
import com.galewings.entity.Feed;
import com.galewings.entity.FeedKeyword;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FeedKeywordRepository {

    private final SqlManager sqlManager;

    public FeedKeywordRepository(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public int insertKeyword(FeedKeyword feedKeyword) {
        return sqlManager.insertEntity(feedKeyword);
    }

    /**
     * 解析済みか判定する
     *
     * @param feedUuid フィードID
     * @param feedLink フィードリンク
     * @return true:解析済み false:未解析
     */
    public boolean isAnalysed(String feedUuid, String feedLink) {
        Map<String, String> params = new HashMap<>();
        params.put("feed_uuid", feedUuid);
        params.put("feed_link", feedLink);
        int count = sqlManager.getCount(new ClasspathSqlResource("sql/feedKeyword/count_analysed.sql"), params);
        return count > 0;
    }

    public List<KeywordPrevDayComparisonDto> selectKeywordPrevDayComparison(String targetDate) {
        Map<String, String> params = new HashMap<>();
        params.put("targetDate", targetDate);
        return sqlManager.getResultList(KeywordPrevDayComparisonDto.class, new ClasspathSqlResource("sql/feedKeyword/keyword_prev_day_comparison.sql"), params);
    }

    public List<Feed> selectKeywordFeed(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        return sqlManager.getResultList(Feed.class, new ClasspathSqlResource("sql/feedKeyword/select_keyword_feed.sql"), params);
    }
}
