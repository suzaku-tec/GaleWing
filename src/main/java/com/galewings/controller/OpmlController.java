package com.galewings.controller;

import be.ceau.opml.OpmlParseException;
import be.ceau.opml.OpmlParser;
import be.ceau.opml.entity.Opml;
import com.galewings.entity.Site;
import com.galewings.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpmlController
 * <p>
 * opml関連の操作を提供する
 */
@RequestMapping("/opml")
@RestController
public class OpmlController {

    /**
     * opmlTemplateEngine
     */
    @Autowired
    private TemplateEngine opmlTemplateEngine;

    /**
     * siteRepository
     */
    @Autowired
    private SiteRepository siteRepository;

    /**
     * OPMLファイルエクスポート
     * <p>
     * 全サイトの情報をOPMLファイルに出力する
     *
     * @return OPMLファイル
     * @throws UnsupportedEncodingException エンコード未サポートエラー
     */
    @GetMapping(value = "/export")
    public ResponseEntity<byte[]> export() throws UnsupportedEncodingException {
        List<Site> allSite = siteRepository.getAllSite();

        Context context = new Context();

        List<Map<String, String>> collect = allSite.stream().map(Outline::new)
                .map(this::getFieldMap)
                .toList();
        context.setVariable("outlines", collect);
        context.setVariable("title", "GaleWings");
        context.setVariable("date", LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

        String opmlStr = opmlTemplateEngine.process("export", context);

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "text/opml");
        header.add("Content-Disposition",
                "attachment; filename*=utf-8''" + URLEncoder.encode("export.opml", StandardCharsets.UTF_8));
        return new ResponseEntity<>(opmlStr.getBytes(StandardCharsets.UTF_8), header, HttpStatus.OK);
    }

    /**
     * OPMLファイルのインポート
     *
     * @param multipartFile OPMLファイル情報
     * @throws IOException        入出力エラー
     * @throws OpmlParseException OPMLパースエラー
     */
    @PostMapping(value = "/import")
    public void importOpml(@RequestParam("file") MultipartFile multipartFile)
            throws IOException, OpmlParseException {
        if (multipartFile.isEmpty()) {
            return;
        }

        byte[] bytes = multipartFile.getBytes();
        Opml opml = new OpmlParser().parse(new String(bytes));
        opml.getBody().getOutlines().forEach(this::importOpml);
    }

    /**
     * OPMLファイルのインポート
     * <p>
     * OPMLファイルから読み取ったデータをDBに登録する
     *
     * @param outline サイトデータ
     */
    private void importOpml(be.ceau.opml.entity.Outline outline) {
        if ("rss".equals(outline.getAttribute("type"))) {
            siteRepository.insertSite(outline);
        } else {
            outline.getSubElements().forEach(this::importOpml);
        }
    }

    /**
     * サイト情報
     */
    private static class Outline {

        String title;
        String htmlUrl;
        String xmlUrl;

        public Outline(Site site) {
            title = site.title;
            htmlUrl = site.htmlUrl;
            xmlUrl = site.xmlUrl;
        }
    }

    /**
     * サイト情報をMapデータに変換する
     *
     * @param obj サイト情報
     * @return サイト情報(Map形式)
     */
    private Map<String, String> getFieldMap(Outline obj) {
        Map<String, String> map = new HashMap<>();
        map.put("htmlUrl", obj.htmlUrl);
        map.put("title", obj.title);
        map.put("xmlUrl", obj.xmlUrl);

        return map;
    }

}
