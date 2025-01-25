package com.recpro.pe.learnsync.services.forum;

import com.recpro.pe.learnsync.dtos.forum.topic.CreateTopicDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Category;
import com.recpro.pe.learnsync.models.Topic;
import com.recpro.pe.learnsync.repos.forum.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryService categoryService;

    public List<TopicDTO> listTopics(Pageable pageable) {
        return topicRepository.findAll(pageable).stream().map(Topic::toDTO).toList();
    }

    public TopicDTO createTopic(CreateTopicDTO request) {
        String nameTransformed = Topic.transformName(request.getName());
        if (topicRepository.existsTopicByName(Topic.transformName(nameTransformed))) {
            throw new ResourceAlreadyExistsException("El tópico " + nameTransformed + " ya existe");
        }
        String slug = request.getName().replaceAll(" ", "-").toLowerCase();
        Category category = categoryService.getCategory(request.getCategoryName());
        Topic topic = new Topic(null, nameTransformed, request.getDescription(), slug, category, new ArrayList<>());
        topicRepository.save(topic);
        return Topic.toDTO(topic);
    }

    public Topic getTopic(String slug) {
        return topicRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotExistsException("El tópico " + Topic.transformName(slug.replaceAll("-", " ")) + " no existe"));
    }

}
