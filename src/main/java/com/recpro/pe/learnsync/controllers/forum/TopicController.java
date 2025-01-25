package com.recpro.pe.learnsync.controllers.forum;

import com.recpro.pe.learnsync.dtos.forum.topic.CreateTopicDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.models.Topic;
import com.recpro.pe.learnsync.services.forum.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("topic")
public class TopicController {

    @Autowired private TopicService topicService;

    @GetMapping("/list/")
    public ResponseEntity<List<TopicDTO>> listTopics(@RequestParam int page) {
        return new ResponseEntity<>(topicService.listTopics(PageRequest.of(page, 10)), HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<TopicDTO> getTopic(@PathVariable String slug) {
        return new ResponseEntity<>(Topic.toDTO(topicService.getTopic(slug)), HttpStatus.FOUND);
    }

    @PostMapping("/create/")
    public ResponseEntity<TopicDTO> createTopic(@Valid @RequestBody CreateTopicDTO request) {
        return new ResponseEntity<>(topicService.createTopic(request), HttpStatus.CREATED);
    }
}
