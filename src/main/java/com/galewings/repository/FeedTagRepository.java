package com.galewings.repository;

import com.galewings.dto.tag.SiteTagInfo;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
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
}
