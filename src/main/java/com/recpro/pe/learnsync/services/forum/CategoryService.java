package com.recpro.pe.learnsync.services.forum;

import com.recpro.pe.learnsync.dtos.forum.category.CategoryDTO;
import com.recpro.pe.learnsync.dtos.forum.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Category;
import com.recpro.pe.learnsync.repos.forum.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> listCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream().map(Category::toDTO).toList();
    }

    public CategoryDTO createCategory(CreateCategoryDTO request) {
        if(categoryRepository.existsCategoryByName(request.getName())) {
            throw new ResourceAlreadyExistsException("La categoría "+ request.getName() +" existe");
        }
        Category category = new Category(null, request.getName(), new ArrayList<>());
        categoryRepository.save(category);
        return Category.toDTO(category);
    }

    public Category getCategory(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotExistsException("La categoria "+name+" no existe"));
    }
}
