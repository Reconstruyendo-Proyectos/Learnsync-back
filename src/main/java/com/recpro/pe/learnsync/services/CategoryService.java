package com.recpro.pe.learnsync.services;

import com.recpro.pe.learnsync.dtos.category.CategoryDTO;
import com.recpro.pe.learnsync.dtos.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.dtos.topic.TopicDTO;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.mappers.CategoryMapper;
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

    @Autowired private CategoryMapper categoryMapper;

    public List<CategoryDTO> listCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream().map(it -> categoryMapper.toDTO(it)).toList();
    }

    public CategoryDTO createCategory(CreateCategoryDTO request) {
        if(categoryRepository.existsCategoryByName(request.getName())) {
            throw new ResourceAlreadyExistsException("La categoría "+ request.getName() +" existe");
        }
        Category category = new Category(null, request.getName(), request.getDescription(), new ArrayList<>());
        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    public Category getCategory(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotExistsException("La categoria "+name+" no existe"));
    }

    /*
    private CategoryDTO returnCategoryDTO(Category category) {
        List<TopicDTO> topics = new ArrayList<>();
        for(Topic topic : category.getTopics()) {
            TopicDTO topicDTO = new TopicDTO(topic.getIdTopic(), topic.getName(), topic.getDescription());
            topics.add(topicDTO);
        }
        return new CategoryDTO(category.getIdCategory(), category.getName(), category.getDescription(), topics);
    }
     */
}
