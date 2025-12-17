package com.galewings.service;

import com.galewings.entity.custom.rss.Rss;
import com.galewings.entity.custom.rss.Site;
import com.galewings.repository.custom.rss.CustomRssRssRepository;
import com.galewings.repository.custom.rss.CustomRssSiteRepository;
import com.galewings.util.WuDiffLines;
import com.galewings.util.stream.Result;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomRssService {

    private final CustomRssSiteRepository customRssSiteRepository;

    private final CustomRssRssRepository customRssRssRepository;

    private static final String LF = "\n";

    private static final Logger logger = LoggerFactory.getLogger(CustomRssService.class);

    @Autowired
    public CustomRssService(CustomRssSiteRepository customRssSiteRepository, CustomRssRssRepository customRssRssRepository) {
        this.customRssSiteRepository = customRssSiteRepository;
        this.customRssRssRepository = customRssRssRepository;
    }

    /**
     * 追跡対象ページの差分抽出
     */
    public void extractDiff() {

        List<Site> sites = customRssSiteRepository.selectAll();

        sites.stream().map(site -> Result.runCatching(() -> diffLinkList(site)).onFailure(e -> logger.error("error diffLinkList", e)))
                .filter(Result::isSuccess)
                .map(Result::getOrNull)
                .flatMap(Collection::stream)
                .forEach(customRssRssRepository::insert);
    }

    /**
     * 最新情報を取得し、内部で保持しているページ情報との差分から新規に追加された内容を取得する。
     *
     * @param site サイト情報
     * @return 差分リンクリスト
     * @throws IOException
     */
    private List<Rss> diffLinkList(Site site) throws IOException {
        Document document = Jsoup.connect(site.url).get();
        String body = document.body().html();
        Path targetFilePath = new ClassPathResource(site.pageFilePath).getFile().toPath();
        List<String> pageStrList = Files.readAllLines(targetFilePath);

        // 差分がないので、後続処理不要
        if (pageStrList.stream().collect(Collectors.joining(CustomRssService.LF)).equals(body)) {
            return Collections.emptyList();
        }

        // 新情報にアップデート
        ClassPathResource cpr = new ClassPathResource(site.pageFilePath);
        if (!cpr.exists()) {
            site.pageFilePath = "txt/customRssHtml/" + site.uuid + ".txt";
            customRssSiteRepository.update(site);
        }
        Files.writeString(cpr.getFile().toPath(), body, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        String[] nowBodyArray = body.split(CustomRssService.LF);
        String[] preBodyArray = pageStrList.toArray(new String[]{});

        // 新規差分を抽出
        List<WuDiffLines.Diff> diff = WuDiffLines.diff(preBodyArray, nowBodyArray);
        String insertList = diff.stream()
                .filter(d -> d.type == WuDiffLines.EditType.INSERT)
                .map(d -> d.line).collect(Collectors.joining(CustomRssService.LF));
        Document diffDoc = Jsoup.parse(insertList);
        Elements el = diffDoc.select("a[href]");

        // 差分の中からリンク情報取得
        List<Rss> linkList = new ArrayList<>();
        for (Element e : el) {
            String href = e.attr("href");

            boolean oldExist = pageStrList.stream().anyMatch(line -> line.contains(href));

            if (!oldExist) {
                String text = findFirstText(e);
                Rss rss = new Rss();
                rss.uuid = UUID.randomUUID().toString();
                rss.link = href;
                rss.title = text.isEmpty() ? href : text;
                linkList.add(rss);
            }
        }

        Files.write(targetFilePath, Arrays.asList(nowBodyArray));

        return linkList;
    }

    /**
     * 最初の文字列を要素の中から再帰的に探す
     *
     * @param element 要素
     * @return 最初の文字列※ない場合は空文字
     */
    private String findFirstText(Element element) {
        if (element.children().isEmpty() && !element.text().trim().isEmpty()) {
            return element.text();
        }

        for (Element child : element.children()) {
            String found = findFirstText(child);
            if (!found.isEmpty()) {
                return found;
            }
        }
        return StringUtils.EMPTY;
    }

}
