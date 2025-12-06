package com.galewings.service;

import com.galewings.entity.Feed;
import com.galewings.entity.FunctionCtrl;
import com.galewings.repository.FunctionCtrlRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

/**
 * FeedFactory
 */
@Service
public class FeedFactoryService {

    private final FunctionCtrlRepository functionCtrlRepository;

    @Autowired
    public FeedFactoryService(FunctionCtrlRepository functionCtrlRepository) {
        this.functionCtrlRepository = functionCtrlRepository;
    }

    /**
     * Feed生成
     *
     * @param syndEntry SyndEntry
     * @param uuid      UUID
     * @return Feed
     */
    public Feed create(SyndEntry syndEntry, String uuid) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Feed f = new Feed();
        f.uuid = uuid;
        f.title = syndEntry.getTitle();
        f.link = syndEntry.getLink();
        f.uri = syndEntry.getUri();
        f.author = syndEntry.getAuthor();
        f.comments = syndEntry.getComments();

        try {
            f.publishedDate = sdFormat.format(syndEntry.getPublishedDate());
        } catch (NullPointerException e) {
            f.publishedDate = null;
        }

        FunctionCtrl functionCtrl = functionCtrlRepository.get("feed-img");
        if ("1".equals(functionCtrl.flg)) {
            f.imageUrl = searchImageUrl(syndEntry);
        }

        f.contentTerxt = createContentText(syndEntry);

        return f;
    }

    private String searchImageUrl(SyndEntry syndEntry) {
        return syndEntry.getContents().stream().map(syndContent -> {
                    if ("text/html".equals(syndContent.getType()) || "html".equals(syndContent.getType())) {
                        Document doc = Jsoup.parseBodyFragment(syndContent.getValue());
                        Element imgEl = doc.select("img").first();
                        if (imgEl != null) {
                            return imgEl.attr("src");
                        }
                    }

                    return null;
                }).filter(StringUtils::isNotEmpty)
                .findFirst().orElseGet(() -> null);
    }

    private String createContentText(SyndEntry syndEntry) {
        return syndEntry.getContents().stream().filter(syndContent -> isHtmlType(syndContent.getType()))
                .map(syndContent -> Jsoup.parseBodyFragment(syndContent.getValue()))
                .map(document -> document.text()).collect(
                        Collectors.joining());
    }

    private boolean isHtmlType(String type) {
        return "text/html".equals(type) || "html".equals(type);
    }
}
