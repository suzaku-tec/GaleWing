package com.galewings.repository.custom.rss;

import com.galewings.entity.custom.rss.Rss;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CustomRssRssRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    CustomRssRssRepository customRssRssRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        when(sqlManager.insertEntity(any())).thenReturn(0);

        int result = customRssRssRepository.insert(new Rss());
        assertEquals(0, result);
    }

    @Test
    void testUpdate() {
        when(sqlManager.updateEntity(any())).thenReturn(0);

        int result = customRssRssRepository.update(new Rss());
        assertEquals(0, result);
    }

    @Test
    void testDelete() {
        when(sqlManager.deleteEntity(any())).thenReturn(0);

        int result = customRssRssRepository.delete(new Rss());
        assertEquals(0, result);
    }

    @Test
    void testSelectAll() {
        when(sqlManager.getResultList(any(), any())).thenReturn(Collections.emptyList());

        List<Rss> result = customRssRssRepository.selectAll(new Rss());
        assertEquals(0, result.size());
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme