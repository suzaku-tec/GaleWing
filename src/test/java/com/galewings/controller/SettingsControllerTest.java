package com.galewings.controller;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.galewings.GaleWingsJson;
import com.galewings.ModelMock;
import com.galewings.TestEntityObject;
import com.galewings.dto.input.SettingsUpdateForm;
import com.galewings.entity.Settings;
import com.galewings.exception.GaleWingsZeroUpdateException;
import com.galewings.repository.SettingRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

class SettingsControllerTest {

  @Mock
  SettingRepository settingRepository;
  @InjectMocks
  SettingsController settingsController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testIndex() {
    // モック設定
    when(settingRepository.getSettingAllList()).thenReturn(
        Arrays.asList(TestEntityObject.settings()));
    Model model = new ModelMock();

    // テスト実施
    String result = settingsController.index(model);

    // 評価
    Assertions.assertEquals("settings", result);
    // TODO modelの中身は評価すべきだが、実施方法検討中
  }

  @Test
  void testUpdate() {
    when(settingRepository.update(anyString(), anyString())).thenReturn(1);

    SettingsUpdateForm form = new SettingsUpdateForm();
    Map<String, String> map = new HashMap<>();
    map.put("1", "2");
    form.setSettings(map);

    String result = settingsController.update(form, null);
    Assertions.assertEquals("redirect:/settings", result);
  }

  @Test
  void testUpdateZero() {
    when(settingRepository.update(anyString(), anyString())).thenReturn(0);

    SettingsUpdateForm form = new SettingsUpdateForm();
    Map<String, String> map = new HashMap<>();
    map.put("1", "2");
    form.setSettings(map);

    try {
      String result = settingsController.update(form, null);
      assert false;
    } catch (GaleWingsZeroUpdateException e) {
    } catch (Exception e) {
      assert false;
    }
  }

  @Test
  void testGetAllJson() {
    when(settingRepository.getSettingAllList()).thenReturn(
        Arrays.asList(TestEntityObject.settings()));

    List<Settings> result = settingsController.getAllJson();
    assetList(Arrays.asList(TestEntityObject.settings()), result);
  }

  private void assetList(List<?> list1, List<?> list2) {

    Assertions.assertEquals(list1.size(), list2.size());

    for (int i = 0; i < list1.size(); i++) {
      Assertions.assertEquals(GaleWingsJson.toJson(list1.get(i)),
          GaleWingsJson.toJson(list2.get(i)));
    }
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme