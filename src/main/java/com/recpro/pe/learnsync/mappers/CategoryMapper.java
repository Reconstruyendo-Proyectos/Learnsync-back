package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.forum.category.CategoryDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.models.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    private final TopicMapper topicMapper;

    public CategoryMapper(TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    public CategoryDTO toDto(Category category) {
        if (category == null) return null;
        List<TopicDTO> topics = category.getTopics() == null ? List.of()
                : category.getTopics().stream().map(topicMapper::toDto).toList();
        return new CategoryDTO(category.getIdCategory(), category.getName(), topics);
    }
}
