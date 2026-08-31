package com.recpro.pe.learnsync.modules.forum.service;

import com.recpro.pe.learnsync.modules.forum.dto.category.CategoryDTO;
import com.recpro.pe.learnsync.modules.forum.dto.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.shared.exception.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.shared.exception.ResourceNotExistsException;
import com.recpro.pe.learnsync.modules.forum.mapper.CategoryMapper;
import com.recpro.pe.learnsync.modules.forum.model.Category;
import com.recpro.pe.learnsync.modules.forum.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDTO> listCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream().map(categoryMapper::toDto).toList();
    }

    public CategoryDTO createCategory(CreateCategoryDTO request) {
        if(categoryRepository.existsCategoryByName(request.getName())) {
            throw new ResourceAlreadyExistsException("La categoría "+ request.getName() +" existe");
        }
        Category category = new Category(null, request.getName(), new ArrayList<>());
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public Category getCategory(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotExistsException("La categoria "+name+" no existe"));
    }
}
