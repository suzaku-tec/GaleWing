package com.example.galewings.repository;

import com.example.galewings.entity.Site;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SiteRepository {
    @Autowired
    SqlManager sqlManager;

    public Site getSite(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        Site site = sqlManager.getSingleResult(Site.class, new ClasspathSqlResource("sql/select_site_origin.sql"), params);
        return site;
    }

    public List<Site> getAllSite() {
        return sqlManager.getResultList(Site.class, new ClasspathSqlResource("sql/select_all_site.sql"));
    }
}
