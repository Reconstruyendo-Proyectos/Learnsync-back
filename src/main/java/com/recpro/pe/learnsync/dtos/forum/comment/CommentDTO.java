package com.recpro.pe.learnsync.dtos.forum.comment;

import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Integer idComment;
    private String message;
    private LocalDateTime creationDate;
    private UserDTO user;
}
