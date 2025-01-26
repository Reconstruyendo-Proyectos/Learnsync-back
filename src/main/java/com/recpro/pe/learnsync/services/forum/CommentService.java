package com.recpro.pe.learnsync.services.forum;

import com.recpro.pe.learnsync.dtos.forum.comment.CommentDTO;
import com.recpro.pe.learnsync.dtos.forum.comment.CreateCommentDTO;
import com.recpro.pe.learnsync.models.Comment;
import com.recpro.pe.learnsync.models.Thread;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.forum.CommentRepository;
import com.recpro.pe.learnsync.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;
    @Autowired private UserService userService;
    @Autowired private ThreadService threadService;

    public List<CommentDTO> listComments(Pageable pageable) {
        return commentRepository.findAll(pageable).stream().map(Comment::toDto).toList();
    }

    public CommentDTO createComment(CreateCommentDTO request) {
        Thread thread = threadService.getThread(request.getIdThread());
        User user = userService.findByUser(request.getUsername());
        Comment comment = new Comment(null, request.getMessage(), thread, user);
        commentRepository.save(comment);
        return Comment.toDto(comment);
    }
}
