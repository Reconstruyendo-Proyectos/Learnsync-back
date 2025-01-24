package com.recpro.pe.learnsync.controllers;

import com.recpro.pe.learnsync.dtos.category.CategoryDTO;
import com.recpro.pe.learnsync.dtos.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired private CategoryService categoryService;

    @GetMapping("/list/")
    public List<CategoryDTO> getCategories(Pageable pageable) {
        return categoryService.listCategory(pageable);
    }

    @PostMapping("/create/")
    public CategoryDTO createCategory(@Valid @RequestBody CreateCategoryDTO request) {
        return categoryService.createCategory(request);
    }
}
