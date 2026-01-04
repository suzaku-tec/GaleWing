package com.galewings.repository;

import com.galewings.dto.statistics.ReadRateDto;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StaticsRepository {

    public final SqlManager sqlManager;

    @Autowired
    public StaticsRepository(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }


    /**
     * 読了率（Read Ratio）の計算に必要な情報の取得
     * <p>
     * 読了率 (%)=(配信された全記事数クリック・再生された記事数​)×100
     * <p>
     * 分母（Total Delivered）: そのフィードから配信された全記事数（一定期間内）
     * <p>
     * 分子（Active Reads）: ユーザーが実際にクリックして詳細を開いた、または動画を再生した数
     * <p>
     * 除外すべきデータ: 「一括既読（Mark all as read）」で処理された記事は、分子には含めず「スルーされた」とみなします。
     *
     * @return
     */
    public ReadRateDto selectReadRate() {
        return sqlManager.getSingleResult(ReadRateDto.class, new ClasspathSqlResource("sql/statics/selectReadRate.sql"));
    }
}
