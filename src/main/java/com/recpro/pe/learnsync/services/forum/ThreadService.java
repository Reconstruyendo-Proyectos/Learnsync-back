package com.recpro.pe.learnsync.services.forum;

import com.recpro.pe.learnsync.dtos.forum.thread.CreateThreadDTO;
import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Thread;
import com.recpro.pe.learnsync.models.Topic;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.forum.ThreadRepository;
import com.recpro.pe.learnsync.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThreadService {
    @Autowired private ThreadRepository threadRepository;
    @Autowired private UserService userService;
    @Autowired private TopicService topicService;

    public List<ThreadDTO> listThreads(Pageable pageable) {
        return threadRepository.findAll(pageable).stream().map(Thread::toDTO).toList();
    }

    public ThreadDTO createThread(CreateThreadDTO request) {
        User user = userService.findByUser(request.getUsername());
        Topic topic = topicService.getTopic(request.getSlug());
        Thread thread = new Thread(null, request.getTitle(), request.getMessage(), topic, user, new ArrayList<>());
        threadRepository.save(thread);
        return Thread.toDTO(thread);
    }

    public Thread getThread(Integer id) {
        return threadRepository.findById(id).orElseThrow(() -> new ResourceNotExistsException("No existe el hilo #"+id));
    }
}
