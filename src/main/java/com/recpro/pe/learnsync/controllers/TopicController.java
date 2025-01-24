package com.recpro.pe.learnsync.controllers;

import com.recpro.pe.learnsync.dtos.topic.CreateTopicDTO;
import com.recpro.pe.learnsync.dtos.topic.TopicDTO;
import com.recpro.pe.learnsync.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("topic")
public class TopicController {

    @Autowired private TopicService topicService;

    @GetMapping("/list/")
    public List<TopicDTO> listTopics(Pageable pageable) {
        return topicService.listTopics(pageable);
    }

    @PostMapping("/create/")
    public TopicDTO createTopic(@RequestBody CreateTopicDTO request) {
        return topicService.createTopic(request);
    }
}
