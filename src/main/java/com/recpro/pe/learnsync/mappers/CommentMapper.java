package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.dtos.forum.comment.CommentDTO;
import com.recpro.pe.learnsync.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    @Autowired private UserMapper userMapper;

    public CommentDTO toDto(Comment comment) {
        UserDTO user = userMapper.toDto(comment.getUser());
        return new CommentDTO(comment.getIdComment(), comment.getMessage(), comment.getCreationDate(), user);
    }
}
