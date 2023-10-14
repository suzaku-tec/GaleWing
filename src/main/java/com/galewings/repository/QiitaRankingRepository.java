package com.galewings.repository;

import com.galewings.dto.qiita.QiitaItemsDto;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class QiitaRankingRepository {

    @Autowired
    private SqlManager sqlManager;

    public int insert(QiitaItemsDto dto) {
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/qiitaRanking/insert.sql"), dto);
    }

    public int allDelete() {
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/qiitaRanking/allDelete.sql"));
    }
}
