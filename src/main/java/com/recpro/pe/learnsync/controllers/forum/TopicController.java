package com.recpro.pe.learnsync.controllers.forum;

import com.recpro.pe.learnsync.dtos.forum.topic.CreateTopicDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.services.forum.TopicService;
import jakarta.validation.Valid;
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
    public TopicDTO createTopic(@Valid @RequestBody CreateTopicDTO request) {
        return topicService.createTopic(request);
    }
}
