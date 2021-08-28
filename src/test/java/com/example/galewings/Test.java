package com.example.galewings;

import com.atilika.kuromoji.ipadic.Tokenizer;
import com.example.galewings.entity.FeedContents;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.session.Session;
import com.miragesql.miragesql.session.SessionFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jsoup.Jsoup;

public class Test {

  public static void main(String[] args) {
    Session session = SessionFactory.getSession();
    SqlManager sqlManager = session.getSqlManager();
    session.begin();

    Tokenizer tokenizer = new Tokenizer();

    List<FeedContents> resultList = sqlManager.getResultList(FeedContents.class,
        new ClasspathSqlResource("sql/feedcontents/select_all.sql"));

    Map<String, Integer> rank = new HashMap<>();

    resultList.stream().parallel()
        .filter(feedContents -> "html".equals(feedContents.type))
        .map(feedContents -> Jsoup.parse(feedContents.value).text())
        .flatMap(s -> tokenizer.tokenize(s).stream())
        .filter(token -> "名詞".equals(token.getPartOfSpeechLevel1()))
        .filter(token -> "固有名詞".equals(token.getPartOfSpeechLevel2()))
        .forEach(token -> {
          var key = token.getSurface();
          var value = rank.get(key);
          if (value == null) {
            rank.put(key, 1);
          } else {
            rank.put(key, value + 1);
          }
        });

    List<Entry<String, Integer>> list = new ArrayList<>(rank.entrySet());
    Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    System.out.println(list.subList(0, 10));

    session.commit();
  }
}
