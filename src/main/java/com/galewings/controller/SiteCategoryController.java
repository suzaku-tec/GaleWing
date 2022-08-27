package com.galewings.controller;

import com.galewings.dto.input.AddSiteCategoryDto;
import com.galewings.dto.input.DeleteSiteCategory;
import com.galewings.entity.Category;
import com.galewings.entity.Site;
import com.galewings.repository.CategoryRepository;
import com.galewings.repository.SiteCategoryRepository;
import com.galewings.repository.SiteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/siteCategory")
@Controller
public class SiteCategoryController {

  @Autowired
  SiteRepository siteRepository;

  @Autowired
  SiteCategoryRepository siteCategoryRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @GetMapping
  public String index(@RequestParam(required = true) String uuid, Model model) {

    Site site = siteRepository.getSite(uuid);

    List<Category> siteCategoryList = siteCategoryRepository.selectCategoryFor(uuid);

    List<Category> categoryList = categoryRepository.selectAll();

    model.addAttribute("site", site);
    model.addAttribute("siteCategoryList", siteCategoryList);
    model.addAttribute("categoryList", categoryList);

    return "siteCategory";
  }

  @PostMapping("/add")
  @ResponseBody
  public List<Category> addSiteCategory(@RequestBody AddSiteCategoryDto addSiteCategoryDto) {
    siteCategoryRepository.add(addSiteCategoryDto.categoryUuid, addSiteCategoryDto.siteUuid);

    return siteCategoryRepository.selectCategoryFor(addSiteCategoryDto.siteUuid);
  }

  @PostMapping("/delete")
  @ResponseBody
  public void delete(@RequestBody DeleteSiteCategory deleteSiteCategory) {
    siteCategoryRepository.deleteCategory(deleteSiteCategory.categoryUuid,
        deleteSiteCategory.siteUuid);
  }
}
