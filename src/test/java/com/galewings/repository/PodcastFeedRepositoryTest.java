package com.galewings.repository;

import com.galewings.entity.PodcastFeed;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.SqlResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PodcastFeedRepositoryTest {

    @Mock
    private SqlManager sqlManager;

    @InjectMocks
    private PodcastFeedRepository target;

    private static final String TEST_URL = "https://example.com/podcast";

    @Test
    @DisplayName("insert: エンティティの挿入が成功すること")
    void testInsert() {
        PodcastFeed feed = new PodcastFeed();
        when(sqlManager.insertEntity(any(PodcastFeed.class))).thenReturn(1);

        int result = target.insert(feed);

        assertEquals(1, result);
        verify(sqlManager, times(1)).insertEntity(feed);
    }

    @Test
    @DisplayName("isExist: データが存在する場合にtrueを返すこと")
    void testIsExist_True() {
        when(sqlManager.getCount(any(SqlResource.class), anyMap())).thenReturn(1);

        boolean result = target.isExist(TEST_URL);

        assertTrue(result);
    }

    @Test
    @DisplayName("isExist: データが存在しない場合にfalseを返すこと")
    void testIsExist_False() {
        when(sqlManager.getCount(any(SqlResource.class), anyMap())).thenReturn(0);

        boolean result = target.isExist(TEST_URL);

        assertFalse(result);
    }

    @Test
    @DisplayName("isNotExist: データが存在しない場合にtrueを返すこと")
    void testIsNotExist_True() {
        // isExistがfalseを返すケース
        when(sqlManager.getCount(any(SqlResource.class), anyMap())).thenReturn(0);

        boolean result = target.isNotExist(TEST_URL);

        assertTrue(result);
    }

    @Test
    @DisplayName("selectAll: 全件取得ができること")
    void testSelectAll() {
        List<PodcastFeed> expectedList = Collections.singletonList(new PodcastFeed());
        when(sqlManager.getResultList(eq(PodcastFeed.class), any(SqlResource.class))).thenReturn(expectedList);

        List<PodcastFeed> result = target.selectAll();

        assertEquals(expectedList.size(), result.size());
        verify(sqlManager, times(1)).getResultList(eq(PodcastFeed.class), any(SqlResource.class));
    }

    @Test
    @DisplayName("markRead: 更新が成功し、更新件数が返ること")
    void testMarkRead() {
        when(sqlManager.executeUpdate(any(SqlResource.class), anyMap())).thenReturn(1);

        int result = target.markRead(TEST_URL);

        assertEquals(1, result);
        verify(sqlManager, times(1)).executeUpdate(any(SqlResource.class), anyMap());
    }
}