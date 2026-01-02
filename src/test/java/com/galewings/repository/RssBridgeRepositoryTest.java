package com.galewings.repository;

import com.galewings.dto.RssBridgeKey;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.SqlResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class RssBridgeRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    RssBridgeRepository rssBridgeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        when(sqlManager.executeUpdate(any(SqlResource.class), any(Object.class))).thenReturn(0);

        int result = rssBridgeRepository.insert("id", "json", "title");
        Assertions.assertEquals(0, result);
    }

    @Test
    void testIsExists() {
        when(sqlManager.getCount(any(SqlResource.class), any(Object.class))).thenReturn(0);

        int result = rssBridgeRepository.isExists("jsonId");
        Assertions.assertEquals(0, result);
    }

    @Test
    void testGetJsonList() {
        when(sqlManager.getResultList(any(), any(SqlResource.class), any(Object.class))).thenReturn(emptyList());

        List<String> result = rssBridgeRepository.getJsonList("title");
        Assertions.assertEquals(emptyList(), result);
    }

    @Test
    void testSelectIdList() {
        when(sqlManager.getResultList(any(), any(SqlResource.class))).thenReturn(emptyList());

        List<String> result = rssBridgeRepository.selectIdList();
        Assertions.assertEquals(emptyList(), result);
    }

    @Test
    void testSelectKeyList() {
        when(sqlManager.getResultList(any(), any(SqlResource.class))).thenReturn(emptyList());

        List<RssBridgeKey> result = rssBridgeRepository.selectKeyList();
        Assertions.assertEquals(emptyList(), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme