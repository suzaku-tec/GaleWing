package com.galewings.repository;

import com.galewings.entity.Stats;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatsRepository {
    @Autowired
    SqlManager sqlManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public List<String> getStatsIdList() {
        return sqlManager.getResultList(String.class,
                new ClasspathSqlResource("sql/stats/select_stats_id_list.sql"));
    }

    @Transactional
    public List<Map<String, Object>> executeStatsSql(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Transactional
    public Stats getStats(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return sqlManager.getSingleResult(Stats.class,
                new ClasspathSqlResource("sql/stats/select_stats_id.sql"), params);
    }
}
