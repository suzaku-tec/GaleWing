package com.galewings.controller;

import com.galewings.dto.input.AddCategory;
import com.galewings.dto.input.DeleteCategory;
import com.galewings.entity.Category;
import com.galewings.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/category")
@Controller
public class CategoryController {

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping("")
  public String index(Model model) {

    model.addAttribute("categoryList", categoryRepository.selectAll());

    return "category";
  }

  @PostMapping("add")
  @ResponseBody
  public void addCategory(@RequestBody AddCategory addCategory) {
    Category category = new Category();
    category.name = addCategory.name;
    category.description = addCategory.description;
    categoryRepository.add(category);
  }

  @PostMapping("delete")
  @ResponseBody
  public void delete(@RequestBody DeleteCategory deleteCategory) {
    categoryRepository.delete(deleteCategory.uuid);
  }
}
