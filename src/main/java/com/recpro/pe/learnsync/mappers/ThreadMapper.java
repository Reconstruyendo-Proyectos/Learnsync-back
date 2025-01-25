package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.dtos.forum.comment.CommentDTO;
import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import com.recpro.pe.learnsync.models.Comment;
import com.recpro.pe.learnsync.models.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ThreadMapper {

    @Autowired private CommentMapper commentMapper;
    @Autowired private UserMapper userMapper;

    public ThreadDTO toDTO(Thread thread){
        List<CommentDTO> comments = new ArrayList<>();
        UserDTO user = userMapper.toDto(thread.getUser());
        for (Comment comment : thread.getComments()) {
            CommentDTO commentDTO = commentMapper.toDto(comment);
            comments.add(commentDTO);
        }
        return new ThreadDTO(thread.getIdThread(), thread.getTitle(), thread.getMessage(), thread.getCreationDate(), user, comments);
    }
}
