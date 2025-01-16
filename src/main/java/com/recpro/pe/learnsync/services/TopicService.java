package com.recpro.pe.learnsync.services;

import com.recpro.pe.learnsync.dtos.topic.CreateTopicDTO;
import com.recpro.pe.learnsync.dtos.topic.TopicDTO;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Category;
import com.recpro.pe.learnsync.models.Topic;
import com.recpro.pe.learnsync.repos.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired private TopicRepository topicRepository;

    @Autowired private CategoryService categoryService;

    public List<TopicDTO> listTopics(Pageable pageable) {
        return topicRepository.findAll(pageable).stream().map(this::returnTopicDTO).toList();
    }

    public TopicDTO createTopic(CreateTopicDTO request) {
        if(topicRepository.existsTopicByName(request.getName())) {
            throw new ResourceAlreadyExistsException("El tópico " + request.getName() + " ya existe");
        }
        Category category = categoryService.getCategory(request.getCategoryName());
        Topic topic = new Topic(null, request.getName(), request.getDescription(), category);
        return returnTopicDTO(topicRepository.save(topic));
    }

    public Topic getTopic(String name) {
        return topicRepository.findByName(name).orElseThrow(() -> new ResourceNotExistsException("El tópico " + name + " no existe"));
    }

    public TopicDTO returnTopicDTO(Topic topic) {
        return new TopicDTO(topic.getIdTopic(), topic.getName(), topic.getDescription());
    }
}
