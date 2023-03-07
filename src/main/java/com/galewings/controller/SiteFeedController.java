package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.AddFeedDto;
import com.galewings.dto.ReadAllShowFeedDto;
import com.galewings.dto.ReadDto;
import com.galewings.dto.UpdateFeedDto;
import com.galewings.dto.output.FeedUpdate;
import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.galewings.entity.SiteFeedCount;
import com.galewings.factory.FeedFactory;
import com.galewings.factory.SiteFactory;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.galewings.service.GwDateService;
import com.galewings.service.URLService;
import com.galewings.task.AutoUpdateTask;
import com.google.common.base.Strings;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
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

/**
 * SiteFeedController
 */
@Controller
public class SiteFeedController {

  /**
   * SiteRepository
   */
  @Autowired
  SiteRepository siteRepository;

  /**
   * FeedRepository
   */
  @Autowired
  FeedRepository feedRepository;

  @Autowired
  private URLService urlService;

  @Autowired
  private AutoUpdateTask autoUpdateTask;

  @Autowired
  private GwDateService gwDateService;

  /**
   * 対象サイトのフィードを取得
   *
   * @param uuid サイトUUID
   * @return 対象サイトの未読フィードのデータJSON
   * @throws JsonProcessingException JSON変換失敗
   */
  @GetMapping("/feedlist")
  @ResponseBody
  public String getFeedList(@RequestParam(value = "uuid", required = false) String uuid)
      throws JsonProcessingException {

    List<Feed> feeds;
    if (Strings.isNullOrEmpty(uuid)) {
      feeds = feedRepository.getAllFeed();
    } else {
      feeds = feedRepository.getFeed(uuid);
    }

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(feeds);
  }

  /**
   * 対象ページを既読にする
   *
   * @param dto 既読にするページのリンク情報
   * @return サイトの未読フィード件数リスト
   * @throws UnsupportedEncodingException エンコード失敗例外
   */
  @PostMapping(value = "/read")
  @ResponseBody
  public String readFeed(@RequestBody ReadDto dto)
      throws UnsupportedEncodingException, JsonProcessingException {
    feedRepository.updateReadFeed(dto.getLink());

    List<SiteFeedCount> siteFeedCounts = siteRepository.getSiteFeedCount(dto.getLink());

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(siteFeedCounts);
  }

  /**
   * 対象サイトのフィード情報を更新する
   *
   * @param dto 対象サイト情報
   * @return 対象フィード情報
   * @throws IOException   入出力失敗
   * @throws FeedException フィード読み込み失敗
   */
  @PostMapping(value = "/feed/update")
  @ResponseBody
  public String updateFeed(@RequestBody UpdateFeedDto dto) throws IOException, FeedException {

    List<Feed> feeds;
    if (Strings.isNullOrEmpty(dto.getUuid())) {
      autoUpdateTask.allUpdate();

      feeds = feedRepository.getAllFeed();
    } else {
      Site site = siteRepository.getSite(dto.getUuid());
      LocalDate retainedDate = gwDateService.retainedDate();

      SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(site.xmlUrl)));
      feed.getEntries().stream().filter(syndEntry -> {
            return !feedRepository.existFeed(syndEntry.getLink());
          }).map(syndEntry -> FeedFactory.create(syndEntry, site.uuid))
          .filter(f -> gwDateService.isRetainedDateAfter(f.publishedDate))
          .forEach(feedRepository::insertEntity);

      feeds = feedRepository.getFeed(site.uuid);
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
   * @throws IOException 入出力失敗
   */
  @PostMapping(value = "/addFeed")
  @ResponseBody
  @Transactional
  public void addSiteFeed(@RequestBody AddFeedDto dto) throws IOException {
    String[] schemes = {"http", "https"};
    UrlValidator urlValidator = new UrlValidator(schemes);
    dto.setLink(StringUtils.trimToEmpty(dto.getLink()));
    if (!urlValidator.isValid(dto.getLink())) {
      throw new IllegalArgumentException("不正なURLです");
    }

    List<String> rssUrlList = searchRssUrlList(dto.getLink());

    if (0 < rssUrlList.size()) {
      rssUrlList.stream().forEach(url -> {
        // TODO メソッド化を考える
        try {
          Optional<SyndFeed> syndFeedOptional = getSyndFeed(url);
          syndFeedOptional.ifPresent(syndFeed -> {

            int cnt = siteRepository.countSiteForHtmlUrl(syndFeed.getLink());
            if (0 < cnt) {
              return;
            }

            Site site = SiteFactory.create(url, syndFeed);

            // サイト追加
            siteRepository.insertEntity(site);

            // フィード追加
            syndFeed.getEntries().stream()
                .map(syndEntry -> FeedFactory.create(syndEntry, site.uuid))
                .forEach(feedRepository::insertEntity);
          });
        } catch (Exception e) {
          // 特に何もしない
          e.printStackTrace();
        }
      });
    }
  }

  @PostMapping(value = "/readAllShowFeed", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
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

    if (link.endsWith(".rdf") || link.endsWith(".rss")) {
      return List.of(link);
    }

    if (getSyndFeed(link).isPresent()) {
      return List.of(link);
    }

    try {
      String domain = urlService.getUrlDomain(link);

      Document document = Jsoup.connect(link).get();
      Elements elements = document.select(
          "link[type*='atom+xml'],link[type*='rss+xml'],a[href^='/'],a[href^='" + domain + "']");

      if (0 < elements.size()) {
        return elements.stream().map(element -> {
              if ("link".equals(StringUtils.toRootLowerCase(element.tagName()))) {

                if (StringUtils.isNotBlank(element.attr("href"))) {
                  return element.attr("href");
                } else {
                  return element.text();
                }
              } else if ("a".equals(StringUtils.toRootLowerCase(element.tagName()))) {
                return element.attr("href");
              } else {
                return StringUtils.EMPTY;
              }
            }).filter(StringUtils::isNoneBlank)
            .map(url -> urlService.fixUrl(domain, url)).filter(opt -> opt.isPresent())
            .map(opt -> opt.get()).collect(
                Collectors.toList());
      } else {
        return Collections.EMPTY_LIST;
      }
    } catch (IOException e) {
      return Collections.EMPTY_LIST;
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

}
