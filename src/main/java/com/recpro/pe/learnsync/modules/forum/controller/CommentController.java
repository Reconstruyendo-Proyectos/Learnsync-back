package com.recpro.pe.learnsync.modules.forum.controller;

import com.recpro.pe.learnsync.modules.forum.dto.comment.CommentDTO;
import com.recpro.pe.learnsync.modules.forum.dto.comment.CreateCommentDTO;
import com.recpro.pe.learnsync.modules.forum.service.CommentService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comments")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("")
    public ResponseEntity<List<CommentDTO>> getComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0) page = 0;
        size = Math.clamp(size, 1, 50);
        return new ResponseEntity<>(commentService.listComments(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CreateCommentDTO request) {
        CommentDTO created = commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
