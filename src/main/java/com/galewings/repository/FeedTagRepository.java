package com.galewings.repository;

import com.galewings.dto.tag.SiteTagInfo;
import com.galewings.entity.FeedCategory;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeedTagRepository {

    private final SqlManager sqlManager;

    @Autowired
    public FeedTagRepository(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public void insertSiteTagInfo(SiteTagInfo siteTagInfo, String link) {
        Arrays.asList(siteTagInfo.tags).stream().forEach(tagInfo -> {
            Map<String, String> param = new HashMap<>();
            param.put("link", link);
            param.put("title", siteTagInfo.title);
            param.put("tag", tagInfo.tag);
            param.put("category", tagInfo.category);
            param.put("relevance", tagInfo.relevance);
            param.put("primary_category", siteTagInfo.primaryCategory);
            sqlManager.executeUpdate(new ClasspathSqlResource("sql/feedTag/insertSiteTagInfo.sql"), param);
        });
    }


    @Transactional
    public List<FeedCategory> selectLink(String link) {
        Map<String, String> param = new HashMap<>();
        param.put("link", link);
        return sqlManager.getResultList(FeedCategory.class,
                new ClasspathSqlResource("sql/category/select_link.sql"), param);
    }

    public List<String> selectHighlyRelevantLink(List<String> tags) {
        Map<String, Object> param = new HashMap<>();
        param.put("tags", tags);
        param.put("matchRate", (int) Math.floor(tags.size() * 0.7));
        return sqlManager.getResultList(String.class,
                new ClasspathSqlResource("sql/category/select_highly_relevant_link.sql"), param);
    }
}
