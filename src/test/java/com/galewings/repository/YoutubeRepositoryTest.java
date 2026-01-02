package com.galewings.repository;

import com.galewings.dto.output.YoutubeListSelectChannel;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class YoutubeRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    YoutubeRepository youtubeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectChannelList() {
        when(sqlManager.getResultList(any(), any())).thenReturn(List.of(null));

        List<YoutubeListSelectChannel> result = youtubeRepository.selectChannelList();
        Assertions.assertEquals(List.of(null), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme