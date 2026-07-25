package com.galewings.repository;

import com.galewings.entity.AIRecommendFeed;
import com.galewings.entity.Feed;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AIRecommendRepositoryTest {

    @Mock
    SqlManager sqlManager;
    @InjectMocks
    AIRecommendRepository aiRecommendRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertAIRecommend() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(1);

        int result = aiRecommendRepository.insertAIRecommend("test_id", "test_title");

        assertEquals(1, result);
    }

    @Test
    void insertAIRecommendOrigin() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(1);

        int result = aiRecommendRepository.insertAIRecommendOrigin("test_id", "test_title");

        assertEquals(1, result);
    }

    @Test
    void selectAIRecommndFeed() {
        when(sqlManager.getResultList(any(), any())).thenReturn(List.of());

        List<AIRecommendFeed> resultList = aiRecommendRepository.selectAIRecommndFeed();

        assertEquals(0, resultList.size());
    }

    @Test
    void selectAIRecommendOrigin() {
        when(sqlManager.getResultList(any(), any(), any())).thenReturn(List.of());
        List<Feed> resultList = aiRecommendRepository.selectAIRecommendOrigin("test");
        assertEquals(0, resultList.size());
    }

    @Test
    void deleteAIRecommendOrigin() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(1);
        int result = aiRecommendRepository.deleteAIRecommendOrigin(null);
        assertEquals(1, result);
    }

    @Test
    void deleteAIRecommendFeed() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(1);
        int result = aiRecommendRepository.deleteAIRecommendFeed(null);
        assertEquals(1, result);
    }
}