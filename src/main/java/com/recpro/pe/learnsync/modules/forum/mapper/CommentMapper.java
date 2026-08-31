package com.recpro.pe.learnsync.modules.forum.mapper;

import com.recpro.pe.learnsync.modules.forum.dto.comment.CommentDTO;
import com.recpro.pe.learnsync.modules.forum.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDTO toDto(Comment comment) {
        if (comment == null) return null;
        return new CommentDTO(
                comment.getIdComment(),
                comment.getMessage(),
                comment.getCreationDate(),
                comment.getUser() != null ? comment.getUser().getUsername() : null,
                comment.getUser() != null ? comment.getUser().getProfilePhoto() : null
        );
    }
}
