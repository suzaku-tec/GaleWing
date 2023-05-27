package com.galewings.repository;

import com.galewings.entity.Podcast;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class PodcastRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    PodcastRepository podcastRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectAll() {
        when(sqlManager.getResultList(any(), any())).thenReturn(Collections.emptyList());
        List<Podcast> result = podcastRepository.selectAll();
        Assertions.assertEquals(Collections.emptyList(), result);
    }
}


//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme