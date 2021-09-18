package com.galewings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.AddFeedDto;
import com.galewings.dto.ReadAllShowFeedDto;
import com.galewings.dto.ReadiedDto;
import com.galewings.dto.UpdateFeedDto;
import com.galewings.dto.output.FeedUpdate;
import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.galewings.entity.SiteFeedCount;
import com.galewings.factory.FeedFactory;
import com.galewings.factory.SiteFactory;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.google.common.base.Strings;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class SiteFeedController {

  @Autowired
  SqlManager sqlManager;

  @Autowired
  SiteRepository siteRepository;

  @Autowired
  FeedRepository feedRepository;

  /**
   * 対象サイトのフィードを取得
   *
   * @param uuid サイトUUID
   * @return 対象サイトの未読フィードのデータJSON
   * @throws JsonProcessingException
   */
  @GetMapping("/feedlist")
  @ResponseBody
  @Transactional
  public String getFeedList(@RequestParam(value = "uuid", required = false) String uuid)
      throws JsonProcessingException {

    List<Feed> feeds;
    if (Strings.isNullOrEmpty(uuid)) {
      feeds = sqlManager.getResultList(Feed.class,
          new ClasspathSqlResource("sql/select_all_feed.sql"));
    } else {
      Map<String, String> params = new HashMap<>();
      params.put("uuid", uuid);
      feeds = sqlManager.getResultList(Feed.class,
          new ClasspathSqlResource("sql/select_site_for_uuid.sql"), params);
    }

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(feeds);
  }

  /**
   * 対象ページを既読にする
   *
   * @param dto 既読にするページのリンク情報
   * @return
   * @throws UnsupportedEncodingException
   */
  @PostMapping(value = "/readed")
  @ResponseBody
  @Transactional
  public String readedFeed(@RequestBody ReadiedDto dto)
      throws UnsupportedEncodingException, JsonProcessingException {
    Map<String, String> params = new HashMap<>();
    params.put("link", dto.getLink());
    sqlManager.executeUpdate(
        new ClasspathSqlResource("sql/update_feed_readed.sql")
        , params
    );

    List<SiteFeedCount> siteFeedCounts = siteRepository.getSiteFeedCount(dto.getLink());

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(siteFeedCounts);
  }

  /**
   * 対象サイトのフィード情報を更新する
   *
   * @param dto 対象サイト情報
   * @return 対象フィード情報
   * @throws IOException
   * @throws FeedException
   */
  @PostMapping(value = "/feed/update")
  @ResponseBody
  @Transactional
  public String updateFeed(@RequestBody UpdateFeedDto dto) throws IOException, FeedException {
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    List<Feed> feeds;
    if (Strings.isNullOrEmpty(dto.getUuid())) {
      siteRepository.getAllSite()
          .stream()
          .map(site -> {
            return new SiteFeed() {
              @Override
              public Site getSite() {
                return site;
              }

              @Override
              public Optional<SyndFeed> getOptionalSyndFeed() {
                return getSyndFeed(site.xmlUrl);
              }
            };
          })
          .filter(siteFeed -> siteFeed.getOptionalSyndFeed().isPresent())
          .forEach(siteFeed -> {
            siteFeed.getOptionalSyndFeed().get().getEntries().stream().filter(syndEntry -> {
              return !feedRepository.existFeed(syndEntry.getLink());
            }).map(syndEntry -> {
              return FeedFactory.create(syndEntry, siteFeed.getSite().uuid);
            }).forEach(sqlManager::insertEntity);

          });

      feeds = sqlManager.getResultList(Feed.class,
          new ClasspathSqlResource("sql/select_all_feed.sql"));
    } else {
      Site site = siteRepository.getSite(dto.getUuid());
      SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(site.xmlUrl)));
      feed.getEntries().stream().filter(syndEntry -> {
        return !feedRepository.existFeed(syndEntry.getLink());
      }).peek(syndEntry -> System.out.println(syndEntry.getTitle())).map(syndEntry -> {
        return FeedFactory.create(syndEntry, site.uuid);
      }).forEach(sqlManager::insertEntity);

      Map<String, String> params = new HashMap<>();
      params.put("uuid", site.uuid);
      feeds = sqlManager.getResultList(Feed.class,
          new ClasspathSqlResource("sql/select_site_for_uuid.sql"), params);
    }

    List<SiteFeedCount> siteFeedCounts = siteRepository.getSiteFeedCount();

    FeedUpdate feedUpdate = new FeedUpdate();
    feedUpdate.feeds = feeds;
    feedUpdate.siteFeedCounts = siteFeedCounts;

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(feedUpdate);
  }

  /**
   * サイトとそのサイトのRSS情報をDBに登録する
   *
   * @param dto 追加情報
   * @throws IOException
   */
  @PostMapping(value = "/addFeed")
  @ResponseBody
  @Transactional
  public void addSiteFeed(@RequestBody AddFeedDto dto) throws IOException {
    String[] schemes = {"http", "https"};
    UrlValidator urlValidator = new UrlValidator(schemes);
    if (!urlValidator.isValid(dto.getLink())) {
      throw new IllegalArgumentException("不正なURLです");
    }

    var rssUrlList = searchRssUrlList(dto.getLink());

    if (0 < rssUrlList.size()) {
      var url = rssUrlList.get(0);
      Optional<SyndFeed> syndFeedOptional = getSyndFeed(url);
      syndFeedOptional.ifPresent(syndFeed -> {
        Site site = SiteFactory.create(url, syndFeed);

        // サイト追加
        sqlManager.insertEntity(site);

        // フィード追加
        syndFeed.getEntries().stream().map(syndEntry -> FeedFactory.create(syndEntry, site.uuid))
            .forEach(sqlManager::insertEntity);
      });
    }
  }

  @PostMapping(value = "/readAllShowFeed", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @Transactional
  public String readAllShowFeed(
      @RequestBody ReadAllShowFeedDto readAllShowFeedDto)
      throws JsonProcessingException {
    feedRepository.updateSiteFeedRead(readAllShowFeedDto.getIdentifier());

    List<SiteFeedCount> resultList = siteRepository.getSiteFeedCount();

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(resultList);

  }

  /**
   * サイト内のRSSページへのリンク一覧を取得する
   *
   * @param link サイトリンク
   * @return RSSページへのリンクリスト
   */
  private List<String> searchRssUrlList(String link) {
    String domain = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getHost();
    if (link.indexOf(link) < 0) {
      return Collections.emptyList();
    }

    try {
      Document document = Jsoup.connect(link).get();
      Elements elements = document.select("link[type*='atom+xml'],link[type*='rss+xml']");

      if (0 < elements.size()) {
        return elements.stream().map(element -> element.attr("href")).collect(Collectors.toList());
      } else {
        return Collections.emptyList();
      }
    } catch (IOException e) {
      return Collections.emptyList();
    }
  }

  /**
   * フィード情報の取得
   *
   * @param xmlUrl RSSのURL
   * @return フィード情報
   */
  private Optional<SyndFeed> getSyndFeed(String xmlUrl) {
    Optional<SyndFeed> result;

    try {
      SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(xmlUrl)));
      result = Optional.ofNullable(syndFeed);
    } catch (Exception e) {
      result = Optional.empty();
    }

    return result;
  }

  /**
   * サイトフィード情報
   */
  private interface SiteFeed {

    /**
     * サイト情報
     *
     * @return サイト情報
     */
    Site getSite();

    /**
     * フィード情報
     *
     * @return フィード情報
     */
    Optional<SyndFeed> getOptionalSyndFeed();
  }
}
