package com.galewings.controller;

import com.galewings.dto.input.ExecuteStatsSqlDto;
import com.galewings.entity.Stats;
import com.galewings.repository.StatsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class StatsControllerTest {
    @Mock
    StatsRepository statsRepository;
    @InjectMocks
    StatsController statsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void executeStatsSqlTest1() {
        ExecuteStatsSqlDto dto = new ExecuteStatsSqlDto();
        dto.id = "";

        when(statsRepository.getStats(anyString())).thenReturn(new Stats());

        var result = statsController.executeStatsSql(dto);

        Assertions.assertNull(result);
    }

    @Test
    public void executeStatsSqlTest2() {
        ExecuteStatsSqlDto dto = new ExecuteStatsSqlDto();
        dto.id = "";

        Stats stats = new Stats();
        stats.sql = "select * from dual";

        when(statsRepository.getStats(anyString())).thenReturn(stats);


        Map<String, Object> executeStatsSqlMap1 = new HashMap<>();
        executeStatsSqlMap1.put("test1", "test");
        Map<String, Object> executeStatsSqlMap2 = new HashMap<>();
        executeStatsSqlMap2.put("test2", 0);
        List<Map<String, Object>> executeStatsSqlList = new ArrayList<>();
        executeStatsSqlList.add(executeStatsSqlMap1);
        executeStatsSqlList.add(executeStatsSqlMap2);
        when(statsRepository.executeStatsSql(anyString())).thenReturn(executeStatsSqlList);

        var result = statsController.executeStatsSql(dto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void statsIdListTest() {
        when(statsRepository.getStatsIdList()).thenReturn(Collections.emptyList());
        var list = statsRepository.getStatsIdList();
        Assertions.assertTrue(list.isEmpty());
    }

    @Test
    public void indexTest() {
        Assertions.assertEquals("stats", statsController.index());
    }

}