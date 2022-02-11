package com.galewings.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.galewings.TestEntityObject;
import com.galewings.entity.Settings;
import com.miragesql.miragesql.SqlManager;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SettingRepositoryTest {

  @Mock
  SqlManager sqlManager;
  @InjectMocks
  SettingRepository settingRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testUpdate() {
    when(sqlManager.executeUpdate(any())).thenReturn(0);
    int result = settingRepository.update("id", "value");

    Assertions.assertEquals(0, result);
  }

  @Test
  void testGetSettingAllList() {
    List<Settings> testDataList = Arrays.asList(TestEntityObject.settings());

    when(sqlManager.getResultList(eq(Settings.class), any())).thenReturn(testDataList);
    List<Settings> result = settingRepository.getSettingAllList();

    Assertions.assertEquals(testDataList, result);
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme