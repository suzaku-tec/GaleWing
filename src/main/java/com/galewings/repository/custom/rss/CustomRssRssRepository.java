package com.galewings.repository.custom.rss;

import com.galewings.entity.custom.rss.Rss;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomRssRssRepository {

    private final SqlManager sqlManager;

    @Autowired
    public CustomRssRssRepository(@Qualifier("customRssSqlManager") SqlManager customRssSqlManager) {
        this.sqlManager = customRssSqlManager;
    }

    public int insert(Rss rss) {
        return sqlManager.insertEntity(rss);
    }

    public int update(Rss rss) {
        return sqlManager.updateEntity(rss);
    }

    public int delete(Rss rss) {
        return sqlManager.deleteEntity(rss);
    }

    public List<Rss> selectAll() {
        return sqlManager.getResultList(Rss.class, new ClasspathSqlResource("sql/custom/rss/rss/select_all.sql"));
    }

}
