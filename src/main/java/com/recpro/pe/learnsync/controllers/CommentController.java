package com.recpro.pe.learnsync.controllers;

import com.recpro.pe.learnsync.dtos.comment.CommentDTO;
import com.recpro.pe.learnsync.dtos.comment.CreateCommentDTO;
import com.recpro.pe.learnsync.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired private CommentService commentService;

    @GetMapping("/list/")
    public List<CommentDTO> listComments(Pageable pageable) {
        return commentService.listComments(pageable);
    }

    @PostMapping("/create/")
    public CommentDTO createComment(@Valid @RequestBody CreateCommentDTO request) {
        return commentService.createComments(request);
    }
}
