package com.recpro.pe.learnsync.modules.forum.controller;

import com.recpro.pe.learnsync.modules.forum.dto.thread.CreateThreadDTO;
import com.recpro.pe.learnsync.modules.forum.dto.thread.ThreadDTO;
import com.recpro.pe.learnsync.modules.forum.mapper.ThreadMapper;
import com.recpro.pe.learnsync.modules.forum.service.ThreadService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Threads")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/threads")
public class ThreadController {
    private final ThreadService threadService;
    private final ThreadMapper threadMapper;

    @GetMapping("")
    public ResponseEntity<List<ThreadDTO>> getThreads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false, defaultValue = "creation-date") String sortBy) {

        if (page < 0) page = 0;
        size = Math.clamp(size, 1, 50);
        if (!"creation-date".equals(sortBy) && !"interactions".equals(sortBy)) {
            throw new IllegalArgumentException("sortBy debe ser 'creation-date' o 'interactions'");
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ThreadDTO> threads;

        if (slug != null) {
            threads = threadService.listThreadsByCreationDate(slug, pageRequest);
        } else if ("interactions".equals(sortBy)) {
            threads = threadService.listThreadsByInteractions(pageRequest);
        } else {
            threads = threadService.listThreads(pageRequest);
        }

        return new ResponseEntity<>(threads, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThreadDTO> getThread(@PathVariable int id) {
        return new ResponseEntity<>(threadMapper.toDto(threadService.getThread(id)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ThreadDTO> createThread(@Valid @RequestBody CreateThreadDTO request) {
        ThreadDTO created = threadService.createThread(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}