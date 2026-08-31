package com.recpro.pe.learnsync.modules.forum.service;

import com.recpro.pe.learnsync.modules.forum.dto.thread.CreateThreadDTO;
import com.recpro.pe.learnsync.modules.forum.dto.thread.ThreadDTO;
import com.recpro.pe.learnsync.shared.exception.ResourceNotExistsException;
import com.recpro.pe.learnsync.modules.forum.mapper.ThreadMapper;
import com.recpro.pe.learnsync.modules.forum.model.Thread;
import com.recpro.pe.learnsync.modules.forum.model.Topic;
import com.recpro.pe.learnsync.modules.auth.model.User;
import com.recpro.pe.learnsync.modules.forum.repository.ThreadRepository;
import com.recpro.pe.learnsync.modules.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreadService {
    private final ThreadRepository threadRepository;
    private final UserService userService;
    private final TopicService topicService;
    private final ThreadMapper threadMapper;

    public List<ThreadDTO> listThreads(Pageable pageable) {
        return threadRepository.findAllByOrderByIdThreadDesc(pageable).stream().map(threadMapper::toDto).toList();
    }

    public List<ThreadDTO> listThreadsByCreationDate(String slug, Pageable pageable) {
        Topic topic = topicService.getTopic(slug);
        return threadRepository.findByTopicOrderByIdThreadDesc(topic, pageable).stream().map(threadMapper::toDto).toList();
    }

    public List<ThreadDTO> listThreadsByInteractions(Pageable pageable) {
        return threadRepository.findByOrderByLikesDesc(pageable).stream().map(threadMapper::toDto).toList();
    }

    public ThreadDTO createThread(CreateThreadDTO request) {
        User user = userService.getAuthenticatedUser();
        Topic topic = topicService.getTopic(request.getSlug());
        String file = null;
        if(request.getFile() != null) {
            file = request.getFile();
        }
        Thread thread = new Thread(null, request.getTitle(), request.getMessage(), 0, 0, file, topic, user, new ArrayList<>());
        threadRepository.save(thread);
        return threadMapper.toDto(thread);
    }

    public Thread getThread(Integer id) {
        return threadRepository.findById(id).orElseThrow(() -> new ResourceNotExistsException("No existe el hilo #"+id));
    }
}
