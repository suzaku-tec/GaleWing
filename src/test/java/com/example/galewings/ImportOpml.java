package com.example.galewings;

import be.ceau.opml.OpmlParseException;
import be.ceau.opml.OpmlParser;
import be.ceau.opml.entity.Opml;
import be.ceau.opml.entity.Outline;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.session.Session;
import com.miragesql.miragesql.session.SessionFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImportOpml {

    public static void main(String[] args) {

        try (FileInputStream is = new FileInputStream("C:\\Users\\ikarashi\\Desktop\\test2.opml")) {
            Opml opml = new OpmlParser().parse(is);
            opml.getBody().getOutlines().forEach(ImportOpml::importOpml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OpmlParseException e) {
            e.printStackTrace();
        }
    }

    private static void importOpml(Outline outline) {
        if ("rss".equals(outline.getAttribute("type"))) {
            insertFeed(outline);
        } else {
            outline.getSubElements().forEach(ImportOpml::importOpml);
        }
    }

    private static void insertFeed(Outline outline) {
        Session session = SessionFactory.getSession();
        SqlManager sqlManager = session.getSqlManager();
        session.begin();

        Map<String, String> params = new HashMap<>();
        params.put("htmlUrl", outline.getAttribute("htmlUrl"));
        int count = sqlManager.getCount(
                new ClasspathSqlResource("sql/select_site_for_uuid.sql"),
                params
        );

        if (0 < count) {
            session.rollback();
            return;
        }

        params = new HashMap<>();
        params.put("uuid", UUID.randomUUID().toString());
        params.put("title", outline.getAttribute("text"));
        params.put("htmlUrl", outline.getAttribute("htmlUrl"));
        params.put("xmlUrl", outline.getAttribute("xmlUrl"));

        sqlManager.executeUpdate(new ClasspathSqlResource("sql/import_feed.sql"), params);

        session.commit();
    }
}
