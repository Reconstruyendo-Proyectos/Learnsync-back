package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.models.Topic;
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
