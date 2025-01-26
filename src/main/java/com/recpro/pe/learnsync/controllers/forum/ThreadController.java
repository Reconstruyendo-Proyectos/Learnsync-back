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
@RequestMapping("thread")
public class ThreadController {

    @Autowired private ThreadService threadService;

    @GetMapping("/list/")
    public ResponseEntity<List<ThreadDTO>> listThreads(@RequestParam int page) {
        return new ResponseEntity<>(threadService.listThreads(PageRequest.of(page, 10)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThreadDTO> getThread(@PathVariable int id) {
        return new ResponseEntity<>(Thread.toDTO(threadService.getThread(id)), HttpStatus.FOUND);
    }

    @PostMapping("/create/")
    public ResponseEntity<ThreadDTO> createThread(@Valid @RequestBody CreateThreadDTO request) {
        return new ResponseEntity<>(threadService.createThread(request), HttpStatus.CREATED);
    }
}
