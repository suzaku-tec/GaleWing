package com.example.galewings;

import com.example.galewings.entity.Feed;
import com.example.galewings.entity.Site;
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
import java.text.SimpleDateFormat;
import java.util.List;

public class RssFeed {

  public static void main(String[] args) {
    Session session = SessionFactory.getSession();
    SqlManager sqlManager = session.getSqlManager();
    session.begin();

    List<Site> resultList = sqlManager.getResultList(Site.class, new ClasspathSqlResource(
        "sql/site/select_all_site.sql"));

    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    resultList.forEach(map -> {
      String xmlurl = map.xmlUrl;
      try {
        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(xmlurl)));
        feed.getEntries().stream().map(syndEntry -> {
          Feed f = new Feed();
          f.uuid = map.uuid;
          f.title = syndEntry.getTitle();
          f.link = syndEntry.getLink();
          f.uri = syndEntry.getUri();
          f.author = syndEntry.getAuthor();
          f.comments = syndEntry.getComments();
          f.publishedDate = sdFormat.format(syndEntry.getPublishedDate());
          return f;
        }).forEach(sqlManager::insertEntity);

      } catch (FeedException | IOException e) {
        e.printStackTrace();
      }

    });
    session.commit();

  }


}
