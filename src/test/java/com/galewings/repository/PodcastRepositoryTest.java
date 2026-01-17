package com.galewings.repository;

import com.galewings.entity.Podcast;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PodcastRepositoryTest {

    @Mock
    private SqlManager sqlManager;

    @InjectMocks
    private PodcastRepository podcastRepository;

    private Podcast testPodcast;

    @BeforeEach
    void setUp() {
        testPodcast = new Podcast();
        // 必要に応じてtestPodcastに値をセット
    }

    @Test
    @DisplayName("selectAll: 全件取得が正常に完了すること")
    void testSelectAll() {
        // Arrange
        List<Podcast> expectedList = Collections.singletonList(new Podcast());
        when(sqlManager.getResultList(eq(Podcast.class), any(ClasspathSqlResource.class)))
                .thenReturn(expectedList);

        // Act
        List<Podcast> actualList = podcastRepository.selectAll();

        // Assert
        assertEquals(expectedList, actualList);
        verify(sqlManager, times(1)).getResultList(eq(Podcast.class), any(ClasspathSqlResource.class));
    }

    @Test
    @DisplayName("insert: エンティティの挿入が成功し、更新件数が返ること")
    void testInsert() {
        // Arrange
        int expectedCount = 1;
        when(sqlManager.insertEntity(testPodcast)).thenReturn(expectedCount);

        // Act
        int actualCount = podcastRepository.insert(testPodcast);

        // Assert
        assertEquals(expectedCount, actualCount);
        verify(sqlManager, times(1)).insertEntity(testPodcast);
    }
}