package com.example.galewings;

import com.example.galewings.entity.Feed;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.session.Session;
import com.miragesql.miragesql.session.SessionFactory;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class RssFeed {

    public static void main(String[] args) {
        Session session = SessionFactory.getSession();
        SqlManager sqlManager = session.getSqlManager();
        session.begin();

        List<Map> resultList = sqlManager.getResultList(Map.class, new ClasspathSqlResource("getrssfeed.sql"));

        resultList.stream().forEach(map -> {
            String xmlurl = map.get("xmlurl").toString();
            try {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(xmlurl)));
                feed.getEntries().stream().map(syndEntry -> {
                    Feed f = new Feed();
                    f.title = syndEntry.getTitle();
                    f.link = syndEntry.getLink();
                    f.uri = syndEntry.getUri();
                    f.author = syndEntry.getAuthor();
                    f.comments = syndEntry.getComments();
                    ;
                    f.publishedDate = syndEntry.getPublishedDate().toString();
                    return f;
                }).forEach(f -> {
                    sqlManager.insertEntity(f);
                });

            } catch (FeedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        session.commit();

    }


}
