package com.galewings.repository;

import com.galewings.dto.tag.SiteTagInfo;
import com.galewings.dto.tag.TagInfo;
import com.galewings.entity.FeedCategory;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FeedTagRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    FeedTagRepository feedTagRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsertSiteTagInfo() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);

        SiteTagInfo testData = new SiteTagInfo();
        testData.tags = new TagInfo[]{new TagInfo()};
        feedTagRepository.insertSiteTagInfo(testData, "link");
    }

    @Test
    void testSelectLink() {
        String link = "http://example.com";
        List<FeedCategory> expected = Collections.singletonList(new FeedCategory());
        when(sqlManager.getResultList(eq(FeedCategory.class), any(ClasspathSqlResource.class), any(Map.class)))
                .thenReturn(expected);

        List<FeedCategory> actual = feedTagRepository.selectLink(link);

        assertEquals(expected, actual);
        verify(sqlManager).getResultList(eq(FeedCategory.class), any(ClasspathSqlResource.class), any(Map.class));
    }

    @Test
    void testSelectHighlyRelevantLink() {
        List<String> tags = Arrays.asList("tag1", "tag2", "tag3");
        List<String> expected = Collections.singletonList("http://example.com");
        when(sqlManager.getResultList(eq(String.class), any(ClasspathSqlResource.class), any(Map.class)))
                .thenReturn(expected);

        List<String> actual = feedTagRepository.selectHighlyRelevantLink(tags);

        assertEquals(expected, actual);
        verify(sqlManager).getResultList(eq(String.class), any(ClasspathSqlResource.class), any(Map.class));
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme