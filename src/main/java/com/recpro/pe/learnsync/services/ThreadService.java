package com.recpro.pe.learnsync.services;

import com.recpro.pe.learnsync.dtos.thread.CreateThreadDTO;
import com.recpro.pe.learnsync.dtos.thread.ThreadDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.mappers.ThreadMapper;
import com.recpro.pe.learnsync.models.Thread;
import com.recpro.pe.learnsync.models.Topic;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThreadService {
    @Autowired private ThreadRepository threadRepository;
    @Autowired private ThreadMapper threadMapper;
    @Autowired private UserService userService;
    @Autowired private TopicService topicService;

    public List<ThreadDTO> listThreads(Pageable pageable) {
        return threadRepository.findAll(pageable).stream().map(it -> threadMapper.toDTO(it)).toList();
    }

    public ThreadDTO createThread(CreateThreadDTO request) {
        User user = userService.findByUser(request.getUsername());
        Topic topic = topicService.getTopic(request.getTopicname());
        Thread thread = new Thread(null, request.getTitle(), request.getMessage(), topic, user, new ArrayList<>());
        return threadMapper.toDTO(threadRepository.save(thread));
    }

    public Thread getThread(Integer id) {
        return threadRepository.findById(id).orElseThrow(() -> new ResourceNotExistsException("No existe el hilo #"+id));
    }
}
