package com.recpro.pe.learnsync.modules.forum.mapper;

import com.recpro.pe.learnsync.modules.forum.dto.category.CategoryDTO;
import com.recpro.pe.learnsync.modules.forum.dto.topic.TopicDTO;
import com.recpro.pe.learnsync.modules.forum.model.Category;
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
