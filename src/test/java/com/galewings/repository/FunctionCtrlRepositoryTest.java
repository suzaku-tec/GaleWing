package com.galewings.repository;

import com.galewings.entity.FunctionCtrl;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class FunctionCtrlRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    FunctionCtrlRepository functionCtrlRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(sqlManager.getResultList(eq(FunctionCtrl.class), any())).thenReturn(Collections.emptyList());

        List<FunctionCtrl> result = functionCtrlRepository.getAll();
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testUpdate() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);

        functionCtrlRepository.update("id", "flg");
    }

    @Test
    void testGet() {
        FunctionCtrl expect = new FunctionCtrl();
        when(sqlManager.getSingleResult(any(), any(), any())).thenReturn(expect);

        FunctionCtrl result = functionCtrlRepository.get("id");
        Assertions.assertEquals(expect, result);
    }

    @Test
    void testGetFlg() {
        when(sqlManager.getSingleResult(any(), any(), any())).thenReturn(null);

        String result = functionCtrlRepository.getFlg("id");
        Assertions.assertEquals("0", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme