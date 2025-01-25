package com.recpro.pe.learnsync.dtos.forum.thread;

import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.dtos.forum.comment.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class ThreadDTO {
    private Integer idThread;
    private String title;
    private String message;
    private LocalDateTime creationDate;
    private UserDTO user;
    private List<CommentDTO> comments;
}
