package com.galewings.repository;

import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@Transactional
public class TitleWordRepository {

    @Autowired
    private SqlManager sqlManager;

    /**
     * URLがDBに登録済みか判定する
     *
     * @param link URS
     * @return true:存在 false:未存在
     */
    public boolean isExist(String link) {
        Map<String, String> params = new HashMap<>();
        params.put("link", link);
        Integer count = sqlManager.getSingleResult(
                Integer.class
                , new ClasspathSqlResource("sql/titleWord/select_count_titleWord.sql")
                , params);

        return 0 < count;
    }

    /**
     * URLのタイトルのワードを登録する
     *
     * @param link URL
     * @param word タイトルワード
     * @return 実行結果
     */
    public int insert(String link, String word) {
        Map<String, String> params = new HashMap<>();
        params.put("link", link);
        params.put("word", word);
        return sqlManager.executeUpdate(new ClasspathSqlResource("sql/titleWord/insert_titleWord.sql"), params);
    }
}
