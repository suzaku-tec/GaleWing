package com.galewings.repository;

import com.galewings.entity.Stats;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StatsRepositoryTest {
    @Mock
    SqlManager sqlManager;

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    StatsRepository statsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getStatsIdList() {
        when(sqlManager.getResultList(any(), any())).thenReturn(Collections.emptyList());
        var res = statsRepository.getStatsIdList();
        Assertions.assertNotNull(res);
    }

    @Test
    public void executeStatsSql() {
        when(jdbcTemplate.queryForList(any())).thenReturn(Collections.emptyList());
        var res = statsRepository.executeStatsSql("");
        Assertions.assertNotNull(res);
    }

    @Test
    public void getStats() {
        when(sqlManager.getSingleResult(any(), any(), any())).thenReturn(new Stats());
        var res = statsRepository.getStats("");
        Assertions.assertNotNull(res);
    }
}
