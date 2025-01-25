package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.forum.category.CategoryDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.models.Category;
import com.recpro.pe.learnsync.models.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {

    @Autowired private TopicMapper topicMapper;

    public CategoryDTO toDTO(Category category) {
        List<TopicDTO> topics = new ArrayList<>();
        for (Topic topic : category.getTopics()){
            TopicDTO topicDTO = topicMapper.toDTO(topic);
            topics.add(topicDTO);
        }
        return new CategoryDTO(category.getIdCategory(), category.getName(), category.getDescription(), topics);
    }
}
