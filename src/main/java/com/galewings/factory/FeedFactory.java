package com.galewings.factory;

import com.galewings.entity.Feed;
import com.rometools.rome.feed.synd.SyndEntry;
import java.text.SimpleDateFormat;

public class FeedFactory {

    public static Feed create(SyndEntry syndEntry, String uuid) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

        Feed f = new Feed();
        f.uuid = uuid;
        f.title = syndEntry.getTitle();
        f.link = syndEntry.getLink();
        f.uri = syndEntry.getUri();
        f.author = syndEntry.getAuthor();
        f.comments = syndEntry.getComments();
        f.publishedDate = sdFormat.format(syndEntry.getPublishedDate());

        return f;
    }
}
