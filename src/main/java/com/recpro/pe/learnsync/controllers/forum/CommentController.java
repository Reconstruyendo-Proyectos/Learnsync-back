package com.recpro.pe.learnsync.controllers.forum;

import com.recpro.pe.learnsync.dtos.forum.comment.CommentDTO;
import com.recpro.pe.learnsync.dtos.forum.comment.CreateCommentDTO;
import com.recpro.pe.learnsync.services.forum.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired private CommentService commentService;

    @GetMapping("/list/")
    public ResponseEntity<List<CommentDTO>> listComments(@RequestParam int page) {
        return new ResponseEntity<>(commentService.listComments(PageRequest.of(page, 10)), HttpStatus.OK);
    }

    @PostMapping("/create/")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CreateCommentDTO request) {
        return new ResponseEntity<>(commentService.createComment(request), HttpStatus.CREATED);
    }
}
