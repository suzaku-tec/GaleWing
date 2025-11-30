package com.galewings.repository;

import com.galewings.dto.tag.SiteTagInfo;
import com.galewings.dto.tag.TagInfo;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme