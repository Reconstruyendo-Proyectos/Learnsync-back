package com.recpro.pe.learnsync.modules.forum.service;

import com.recpro.pe.learnsync.modules.forum.dto.comment.CommentDTO;
import com.recpro.pe.learnsync.modules.forum.dto.comment.CreateCommentDTO;
import com.recpro.pe.learnsync.modules.forum.mapper.CommentMapper;
import com.recpro.pe.learnsync.modules.forum.model.Comment;
import com.recpro.pe.learnsync.modules.forum.model.Thread;
import com.recpro.pe.learnsync.modules.auth.model.User;
import com.recpro.pe.learnsync.modules.forum.repository.CommentRepository;
import com.recpro.pe.learnsync.modules.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ThreadService threadService;
    private final CommentMapper commentMapper;

    public List<CommentDTO> listComments(Pageable pageable) {
        return commentRepository.findAll(pageable).stream().map(commentMapper::toDto).toList();
    }

    public CommentDTO createComment(CreateCommentDTO request) {
        Thread thread = threadService.getThread(request.getIdThread());
        User user = userService.findByUser(request.getUsername());
        Comment comment = new Comment(null, request.getMessage(), thread, user);
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }
}
