package com.galewings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.dto.input.SettingsUpdateForm;
import com.galewings.entity.Settings;
import com.galewings.exception.GaleWingsZeroUpdateException;
import com.galewings.repository.SettingRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SettingsController
 */
@RequestMapping("/settings")
@Controller
public class SettingsController {

  @Autowired
  private SettingRepository settingRepository;

  /**
   * 設定ページ初期表示
   *
   * @param model
   * @return 遷移先情報
   * @throws JsonProcessingException
   */
  @GetMapping("")
  @Transactional
  public String index(Model model) throws JsonProcessingException {
    List<Settings> settingsList = settingRepository.getSettingAllList();
    model.addAttribute("settings", settingsList);

    return "settings";
  }

  /**
   * 設定情報更新
   *
   * @param form  更新情報
   * @param model
   * @return 遷移先情報
   */
  @PostMapping("/update")
  @Transactional
  public String update(SettingsUpdateForm form, Model model) {
    form.getSettings().forEach((key, value) -> {
      if (0 == settingRepository.update(key, value)) {
        throw new GaleWingsZeroUpdateException();
      }
    });

    return "redirect:/settings";
  }

  /**
   * 設定情報リスト取得（JSON形式）
   *
   * @return 設定情報リスト（JSON形式）
   */
  @Transactional
  @RequestMapping(value = "/json", method = RequestMethod.GET)
  @ResponseBody
  public List<Settings> getAllJson() {
    return settingRepository.getSettingAllList();
  }
}
