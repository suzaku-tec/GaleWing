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
                DocumentBuilder builder = factory.newDocumentBuilder();
                return Optional.of(builder.parse(new InputSource(new StringReader(xmlData.toString())))) ;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public PodcastFeed createPodcastFeed(String url) {
        PodcastFeed pf = new PodcastFeed();
        pf.id = UUID.randomUUID().toString();
        pf.url = url;
        return pf;
    }

    public void convertText() {
        podcastFeedRepository.selectAll().stream().forEach(podcastFeed -> {
            try {
                String filePath = downloadAudioFile(podcastFeed.url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String downloadAudioFile(String fileUrl) throws IOException {
        String saveFilePath = "tmp.mp3";
        URL url = new URL(fileUrl);
        URLConnection connection = url.openConnection();

        try(InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(saveFilePath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        System.out.println("ファイルのダウンロードが完了しました。");

        return saveFilePath;
    }

}
