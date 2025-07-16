package com.recpro.pe.learnsync.controllers.forum;

import com.recpro.pe.learnsync.dtos.forum.thread.CreateThreadDTO;
import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import com.recpro.pe.learnsync.models.Thread;
import com.recpro.pe.learnsync.services.forum.ThreadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/thread")
public class ThreadController {

    @Autowired private ThreadService threadService;

    @GetMapping("")
    public ResponseEntity<List<ThreadDTO>> getThreads(
            @RequestParam int page,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false, defaultValue = "creation-date") String sortBy) {

        PageRequest pageRequest = PageRequest.of(page, 10);
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
        return new ResponseEntity<>(Thread.toDTO(threadService.getThread(id)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ThreadDTO> createThread(@Valid @RequestBody CreateThreadDTO request) {
        return new ResponseEntity<>(threadService.createThread(request), HttpStatus.CREATED);
    }
}