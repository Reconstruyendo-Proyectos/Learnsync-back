package com.recpro.pe.learnsync.modules.forum.controller;

import com.recpro.pe.learnsync.modules.forum.dto.category.CategoryDTO;
import com.recpro.pe.learnsync.modules.forum.dto.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.modules.forum.service.CategoryService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0) page = 0;
        size = Math.clamp(size, 1, 50);
        return new ResponseEntity<>(categoryService.listCategory(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryDTO request) {
        CategoryDTO created = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
