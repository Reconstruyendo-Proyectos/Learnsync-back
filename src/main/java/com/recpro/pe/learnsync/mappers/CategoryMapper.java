package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.category.CategoryDTO;
import com.recpro.pe.learnsync.models.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    @Autowired private ModelMapper modelMapper;

    public CategoryDTO toDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }
}
