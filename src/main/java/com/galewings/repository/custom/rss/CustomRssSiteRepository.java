package com.galewings.repository.custom.rss;

import com.galewings.entity.custom.rss.Site;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomRssSiteRepository {

    private final SqlManager sqlManager;

    @Autowired
    public CustomRssSiteRepository(@Qualifier("customRssSqlManager") SqlManager customRssSqlManager) {
        this.sqlManager = customRssSqlManager;
    }

    public int insert(String url, String pageFilePath) {
        Site site = new Site();
        site.url = url;
        site.pageFilePath = pageFilePath;
        return insert(site);
    }

    public int insert(Site site) {
        return sqlManager.insertEntity(site);
    }

    public int delete(Site site) {
        return sqlManager.deleteEntity(site);
    }

    public int update(Site site) {
        return sqlManager.updateEntity(site);
    }

    public List<Site> selectAll() {
        return sqlManager.getResultList(Site.class, new ClasspathSqlResource("sql/custom/rss/site/select_all.sql"));
    }
}
