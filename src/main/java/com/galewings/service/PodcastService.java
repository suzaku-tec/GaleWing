package com.galewings.service;

import com.galewings.entity.PodcastFeed;
import com.galewings.repository.PodcastFeedRepository;
import com.galewings.repository.PodcastRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class PodcastService {

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private PodcastFeedRepository podcastFeedRepository;

    public void sync() {
        podcastRepository.selectAll().stream().map(podcast -> {
            return readRssXml(podcast.url);
        }).filter(Optional::isPresent)
                .map(Optional::get)
                .map(document -> document.getElementsByTagName("enclosure"))
                .flatMap(nodeList -> IntStream
                        .range(0, nodeList.getLength())
                        .mapToObj(nodeList::item))
                .map(node -> (Element) node)
                .filter(element -> "audio/mpeg".equals(element.getAttribute("type")))
                .map(element -> element.getAttribute("url"))
                .filter(podcastFeedRepository::isNotExist)
                .map(this::createPodcastFeed)
                .sequential()
                .forEach(podcastFeedRepository::insert);
    }

    private Optional<Document> readRssXml(String url) {
        try {
            URL tmpUrl = new URL(url);

            // URLを開き、データを読み取るためのストリームを作成
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(tmpUrl.openStream(),
                    StandardCharsets.UTF_8))) {
                // 読み取ったデータを格納する文字列バッファ
                StringBuilder xmlData = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    xmlData.append(line).append("\r\n");
                }

                // XMLデータを解析してDOMオブジェクトを取得
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
                factory.setExpandEntityReferences(false);
                DocumentBuilder builder = factory.newDocumentBuilder();
                return Optional.of(builder.parse(new InputSource(new StringReader(xmlData.toString())))) ;
            }

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public PodcastFeed createPodcastFeed(String url) {
        PodcastFeed pf = new PodcastFeed();
        pf.id = UUID.randomUUID().toString();
        pf.url = url;
        return pf;
    }


}
