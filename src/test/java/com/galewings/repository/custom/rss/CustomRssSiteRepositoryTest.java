package com.galewings.repository.custom.rss;

import com.galewings.entity.custom.rss.Site;
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

class CustomRssSiteRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    CustomRssSiteRepository customRssSiteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        when(sqlManager.insertEntity(any())).thenReturn(0);

        int result = customRssSiteRepository.insert("url", "pageFilePath");
        assertEquals(0, result);
    }

    @Test
    void testInsert2() {
        when(sqlManager.insertEntity(any())).thenReturn(0);

        int result = customRssSiteRepository.insert(new Site());
        assertEquals(0, result);
    }

    @Test
    void testDelete() {
        when(sqlManager.deleteEntity(any())).thenReturn(0);

        int result = customRssSiteRepository.delete(new Site());
        assertEquals(0, result);
    }

    @Test
    void testUpdate() {
        when(sqlManager.updateEntity(any())).thenReturn(0);

        int result = customRssSiteRepository.update(new Site());
        assertEquals(0, result);
    }

    @Test
    void testSelectAll() {
        when(sqlManager.getResultList(any(), any())).thenReturn(Collections.emptyList());

        List<Site> result = customRssSiteRepository.selectAll();
        assertEquals(0, result.size());
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme