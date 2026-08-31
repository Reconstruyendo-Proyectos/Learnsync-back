package com.recpro.pe.learnsync.modules.forum.mapper;

import com.recpro.pe.learnsync.modules.forum.dto.topic.TopicDTO;
import com.recpro.pe.learnsync.modules.forum.model.Topic;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {

    public TopicDTO toDto(Topic topic) {
        if (topic == null) return null;
        return new TopicDTO(
                topic.getIdTopic(),
                topic.getName(),
                topic.getDescription(),
                topic.getSlug(),
                topic.getTopicIcon(),
                topic.getTopicPoster(),
                topic.getThreads() != null ? topic.getThreads().size() : 0
        );
    }
}
