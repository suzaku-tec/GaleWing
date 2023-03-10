package com.galewings.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import be.ceau.opml.entity.Outline;
import com.galewings.dto.output.SiteLastFeedDto;
import com.galewings.entity.Site;
import com.galewings.entity.SiteFeedCount;
import com.galewings.service.FaviconService;
import com.galewings.service.GwDateService;
import com.miragesql.miragesql.SqlManager;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SiteRepositoryTest {

  @Mock
  SqlManager sqlManager;
  @Mock
  FaviconService faviconUtil;

  @Mock
  GwDateService gwDateService;

  @InjectMocks
  SiteRepository siteRepository;

  @Test
  void testGetSite() {
    Site testSite = new Site();

    when(sqlManager.getSingleResult(any(), any(), any())).thenReturn(testSite);

    Site result = siteRepository.getSite("uuid");
    Assertions.assertEquals(testSite, result);
  }

  @Test
  void testGetAllSite() {
    List<Object> testSiteList = Collections.singletonList(List.of(new Site()));

    when(sqlManager.getResultList(any(), any())).thenReturn(testSiteList);

    List<Site> result = siteRepository.getAllSite();
    Assertions.assertEquals(testSiteList, result);
  }

  @Test
  void testGetSiteFeedCount() {
    List<Object> testSiteList = Collections.singletonList(
        List.of(new SiteFeedCount()));

    when(sqlManager.getResultList(any(), any())).thenReturn(testSiteList);

    List<SiteFeedCount> result = siteRepository.getSiteFeedCount();
    Assertions.assertEquals(testSiteList, result);
  }

  @Test
  void testGetSiteFeedCount2() {
    List<Object> testSiteList = Collections.singletonList(
        List.of(new SiteFeedCount()));

    when(sqlManager.getResultList(any(), any(), any())).thenReturn(testSiteList);

    List<SiteFeedCount> result = siteRepository.getSiteFeedCount("link");
    Assertions.assertEquals(testSiteList, result);
  }

  @Test
  void testInsertSite() {
    when(sqlManager.getCount(any(), any())).thenReturn(0);
    when(faviconUtil.getBase64Favicon(any())).thenReturn(Optional.empty());
    when(sqlManager.executeUpdate(any(), any())).thenReturn(0);

    Outline testOutline = new Outline(Collections.emptyMap(), Collections.emptyList());

    siteRepository.insertSite(testOutline);
  }

  @Test
  void testDelete() {
    when(sqlManager.executeUpdate(any(), any())).thenReturn(0);

    boolean result = siteRepository.delete("uuid");
    Assertions.assertFalse(result);
  }

  @Test
  void testInsertEntity() {
    when(sqlManager.insertEntity(any())).thenReturn(0);
    int result = siteRepository.insertEntity(new Site());
    Assertions.assertEquals(0, result);
  }

  @Test
  void testUpdateSiteLastUpdateDate() {
    when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
    int result = siteRepository.updateSiteLastUpdateDate("uuid", "lastUpdateDate");
    Assertions.assertEquals(0, result);
  }

  @Test
  void testSelectSiteLastFeed() {
    List<Object> testSlfList = List.of(new SiteLastFeedDto());
    when(sqlManager.getResultList(any(), any())).thenReturn(testSlfList);
    List<SiteLastFeedDto> result = siteRepository.selectSiteLastFeed();
    Assertions.assertEquals(testSlfList, result);
  }

  @Test
  public void testUpdateFeedUpdateDateCheckFormatError() {
    when(gwDateService.checkFormatDate(any(), any())).thenReturn(false);
    int result = siteRepository.updateFeedUpdateDate(null, null);
    Assertions.assertEquals(0, result);
  }

  @Test
  public void testUpdateFeedUpdateDate1() {
    when(gwDateService.checkFormatDate(any(), any())).thenReturn(true);
    when(sqlManager.executeUpdate(any(), any())).thenReturn(1);

    int result = siteRepository.updateFeedUpdateDate("test1", "test2");
    Assertions.assertEquals(1, result);
  }

  @Test
  public void testUpdateFeedUpdateDate2() {
    when(gwDateService.checkFormatDate(any(), any())).thenReturn(true);
    when(sqlManager.executeUpdate(any(), any())).thenReturn(2);

    int result = siteRepository.updateFeedLastUpdateDate("test1", LocalDate.now());
    Assertions.assertEquals(2, result);
  }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme