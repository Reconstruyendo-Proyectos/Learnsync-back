package com.recpro.pe.learnsync.services;

import com.recpro.pe.learnsync.dtos.category.CategoryDTO;
import com.recpro.pe.learnsync.dtos.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Category;
import com.recpro.pe.learnsync.repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> listCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream().map(this::returnCategoryDTO).toList();
    }

    public CategoryDTO createCategory(CreateCategoryDTO request) {
        if(categoryRepository.existsCategoryByName(request.getName())) {
            throw new ResourceAlreadyExistsException("La categoría "+ request.getName() +" existe");
        }
        Category category = new Category(null, request.getName(), request.getDescription()/*, new ArrayList<>()*/);
        return returnCategoryDTO(categoryRepository.save(category));
    }

    public Category getCategory(String name) {
        if(!categoryRepository.existsCategoryByName(name)) {
            throw new ResourceNotExistsException("La categoria "+name+" no existe");
        }
        return categoryRepository.findByName(name);
    }

    private CategoryDTO returnCategoryDTO(Category category) {
        return new CategoryDTO(category.getName(), category.getDescription());
    }
}
