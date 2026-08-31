package com.recpro.pe.learnsync.modules.forum.controller;

import com.recpro.pe.learnsync.modules.forum.dto.category.CategoryDTO;
import com.recpro.pe.learnsync.modules.forum.dto.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.modules.forum.service.CategoryService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Listar categorías", description = "GET paginado ?page=0&size=10 max 50")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0) page = 0;
        size = Math.clamp(size, 1, 50);
        return new ResponseEntity<>(categoryService.listCategory(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Operation(summary = "Crear categoría", description = "Requiere JWT. Nombre único")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Creada"), @ApiResponse(responseCode = "409", description = "Ya existe"), @ApiResponse(responseCode = "401", description = "No autenticado")})
    @PostMapping("")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryDTO request) {
        CategoryDTO created = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
