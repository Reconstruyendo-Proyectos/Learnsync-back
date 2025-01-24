package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.comment.CommentDTO;
import com.recpro.pe.learnsync.models.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    @Autowired private ModelMapper modelMapper;

    public CommentDTO toDto(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }
}
