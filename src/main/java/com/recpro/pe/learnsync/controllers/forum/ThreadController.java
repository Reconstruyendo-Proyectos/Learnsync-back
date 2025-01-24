package com.recpro.pe.learnsync.controllers.forum;

import com.recpro.pe.learnsync.dtos.thread.CreateThreadDTO;
import com.recpro.pe.learnsync.dtos.thread.ThreadDTO;
import com.recpro.pe.learnsync.services.forum.ThreadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("thread")
public class ThreadController {

    @Autowired private ThreadService threadService;

    @GetMapping("/list/")
    public List<ThreadDTO> listThreads(Pageable pageable) {
        return threadService.listThreads(pageable);
    }

    @PostMapping("/create/")
    public ThreadDTO createThread(@Valid @RequestBody CreateThreadDTO request) {
        return threadService.createThread(request);
    }
}
