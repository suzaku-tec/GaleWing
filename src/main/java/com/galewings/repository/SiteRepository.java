package com.galewings.repository;

import com.galewings.dto.output.SiteLastFeedDto;
import com.galewings.entity.Site;
import com.galewings.entity.SiteFeedCount;
import com.galewings.service.FaviconService;
import com.galewings.service.GwDateService;
import com.galewings.service.GwDateService.DateFormat;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * SiteRepository
 */
@Component
@Transactional
public class SiteRepository {

    /**
     * SqlManager
     */
    @Autowired
    SqlManager sqlManager;

    /**
     * FaviconService
     */
    @Autowired
    FaviconService faviconUtil;

    @Autowired
    GwDateService gwDateService;

    private static final String HTML_URL_STR = "htmlUrl";

    /**
     * サイト情報取得
     *
     * @param uuid UUID
     * @return サイト情報
     */

    public Site getSite(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        Site site = sqlManager.getSingleResult(Site.class,
                new ClasspathSqlResource("sql/select_site_origin.sql"), params);
        return site;
    }

    /**
     * 全サイト情報取得
     *
     * @return 全サイト情報リスト
     */

    public List<Site> getAllSite() {
        return sqlManager.getResultList(Site.class, new ClasspathSqlResource(
                "sql/site/select_all_site.sql"));
    }

    /**
     * サイト別フィード件数取得
     *
     * @return サイト別フィード件数リスト
     */

    public List<SiteFeedCount> getSiteFeedCount() {
        return sqlManager.getResultList(SiteFeedCount.class,
                new ClasspathSqlResource("sql/site/select_all_site_count_feed.sql"));
    }

    /**
     * 特定サイトのフィード件数取得
     *
     * @param link リンク
     * @return フィード件数
     */

    public List<SiteFeedCount> getSiteFeedCount(String link) {
        Map<String, String> params = new HashMap<>();
        params.put("link", link);

        return sqlManager.getResultList(SiteFeedCount.class,
                new ClasspathSqlResource("sql/site/select_site_count_for_link.sql"), params);
    }


    public int countSiteForHtmlUrl(String htmlUrl) {
        Map<String, String> params = new HashMap<>();
        params.put(SiteRepository.HTML_URL_STR, htmlUrl);
        int count = sqlManager.getCount(
                new ClasspathSqlResource("sql/site/select_site_for_htmlUrl.sql"),
                params
        );

        return count;
    }

    /**
     * サイト情報追加
     *
     * @param outline
     */

    public void insertSite(be.ceau.opml.entity.Outline outline) {
        Map<String, String> params = new HashMap<>();
        params.put(SiteRepository.HTML_URL_STR, outline.getAttribute(SiteRepository.HTML_URL_STR));
        int count = sqlManager.getCount(
                new ClasspathSqlResource("sql/site/select_site_for_htmlUrl.sql"),
                params
        );

        if (0 < count) {
            return;
        }

        var faviconBase64 = faviconUtil.getBase64Favicon(
                outline.getAttribute(SiteRepository.HTML_URL_STR)).orElse(null);

        params = new HashMap<>();
        params.put("uuid", UUID.randomUUID().toString());
        params.put("title", outline.getAttribute("text"));
        params.put(SiteRepository.HTML_URL_STR, outline.getAttribute(SiteRepository.HTML_URL_STR));
        params.put("xmlUrl", outline.getAttribute("xmlUrl"));
        params.put("faviconBase64", faviconBase64);

        sqlManager.executeUpdate(new ClasspathSqlResource("sql/site/insert_site.sql"), params);
    }

    /**
     * サイト情報削除
     *
     * @param uuid UUID
     * @return 実行結果
     */

    public boolean delete(String uuid) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        int siteDelCnt = sqlManager.executeUpdate(new ClasspathSqlResource("sql/site/delete.sql"),
                params);

        sqlManager.executeUpdate(new ClasspathSqlResource("sql/feed/delete.sql"),
                params);

        return 0 < siteDelCnt;
    }

    /**
     * サイト情報追加
     *
     * @param site サイト情報
     * @return
     */

    public int insertEntity(Site site) {
        return sqlManager.insertEntity(site);
    }


    public int updateSiteLastUpdateDate(String uuid, String lastUpdateDate) {

        Map<String, String> param = new HashMap<>();
        param.put("uuid", uuid);
        param.put("lastUpdateDate", lastUpdateDate);

        return sqlManager.executeUpdate(
                new ClasspathSqlResource("sql/site/update_site_lastUpdate.sql"), param);
    }


    public List<SiteLastFeedDto> selectSiteLastFeed() {
        return sqlManager.getResultList(SiteLastFeedDto.class,
                new ClasspathSqlResource("sql/feed/select_last_feed.sql"));
    }


    public int updateFeedUpdateDate(String uuid, String feedUpdateDate) {
        if (gwDateService.checkFormatDate(feedUpdateDate,
                GwDateService.DateFormat.SQLITE_DATE_FORMAT)) {
            Map<String, String> param = new HashMap<>();
            param.put("uuid", uuid);
            param.put("feedUpdateDate", feedUpdateDate);

            return sqlManager.executeUpdate(
                    new ClasspathSqlResource("sql/site/update_feed_updateDate.sql"), param);
        } else {
            return 0;
        }
    }


    public int updateFeedLastUpdateDate(String uuid, LocalDate ld) {
        String feedUpdateDate = ld.format(DateFormat.SQLITE_DATE_FORMAT.dtf);
        return updateFeedUpdateDate(uuid, feedUpdateDate);
    }

    public void updateSiteIcon(String uuid, String base64) {
        Map<String, String> param = new HashMap<>();
        param.put("uuid", uuid);
        param.put("base64", base64);
        sqlManager.executeUpdate(new ClasspathSqlResource("sql/site/update_site_icon.sql"), param);
    }
}
