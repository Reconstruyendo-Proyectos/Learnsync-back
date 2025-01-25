package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.models.Thread;
import com.recpro.pe.learnsync.models.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TopicMapper {

    @Autowired private ThreadMapper threadMapper;

    public TopicDTO toDTO(Topic topic){
        List<ThreadDTO> threads = new ArrayList<>();
        for (Thread thread : topic.getThreads()) {
            ThreadDTO threadDTO = threadMapper.toDTO(thread);
            threads.add(threadDTO);
        }
        return new TopicDTO(topic.getIdTopic(), topic.getName(), topic.getDescription(), topic.getSlug(), threads);
    }
}
