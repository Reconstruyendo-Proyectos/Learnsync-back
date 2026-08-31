package com.recpro.pe.learnsync.modules.forum.service;

import com.recpro.pe.learnsync.modules.forum.dto.topic.CreateTopicDTO;
import com.recpro.pe.learnsync.modules.forum.dto.topic.TopicDTO;
import com.recpro.pe.learnsync.shared.exception.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.shared.exception.ResourceNotExistsException;
import com.recpro.pe.learnsync.modules.forum.mapper.TopicMapper;
import com.recpro.pe.learnsync.modules.forum.model.Category;
import com.recpro.pe.learnsync.modules.forum.model.Topic;
import com.recpro.pe.learnsync.modules.forum.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final CategoryService categoryService;
    private final TopicMapper topicMapper;

    public List<TopicDTO> listTopics(Pageable pageable) {
        return topicRepository.findAll(pageable).stream().map(topicMapper::toDto).toList();
    }

    public TopicDTO createTopic(CreateTopicDTO request) {
        String nameTransformed = Topic.transformName(request.getName());
        if (topicRepository.existsTopicByName(Topic.transformName(nameTransformed))) {
            throw new ResourceAlreadyExistsException("El tópico " + nameTransformed + " ya existe");
        }
        String slug = request.getName().replace(" ", "-").toLowerCase();
        Category category = categoryService.getCategory(request.getCategoryName());
        Topic topic = new Topic(null, nameTransformed, request.getDescription(), slug, request.getTopicIcon(), request.getTopicPoster(), category, new ArrayList<>());
        topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    public Topic getTopic(String slug) {
        return topicRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotExistsException("El tópico " + Topic.transformName(slug.replace("-", " ")) + " no existe"));
    }
}
