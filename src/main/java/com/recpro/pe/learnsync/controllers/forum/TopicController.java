package com.recpro.pe.learnsync.controllers.forum;

import com.recpro.pe.learnsync.dtos.forum.topic.CreateTopicDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.mappers.TopicMapper;
import com.recpro.pe.learnsync.services.forum.TopicService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Topics")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {
    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @GetMapping("")
    public ResponseEntity<List<TopicDTO>> listTopics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0) page = 0;
        size = Math.min(Math.max(size, 1), 50);
        return new ResponseEntity<>(topicService.listTopics(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<TopicDTO> getTopic(@PathVariable String slug) {
        return new ResponseEntity<>(topicMapper.toDto(topicService.getTopic(slug)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<TopicDTO> createTopic(@Valid @RequestBody CreateTopicDTO request) {
        TopicDTO created = topicService.createTopic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
