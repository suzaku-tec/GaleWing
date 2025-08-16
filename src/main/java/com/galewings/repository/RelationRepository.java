package com.galewings.repository;

import com.galewings.dto.relation.FeedRelation;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RelationRepository {

    @Autowired
    private SqlManager sqlManager;

    public List<FeedRelation> selectFeedRelationList(String uuid) {
        Map map = new HashMap();
        map.put("uuid", uuid);
        return sqlManager.getResultList(FeedRelation.class, new ClasspathSqlResource("sql/relation/select_feed_relation_list.sql"), map);
    }
}
