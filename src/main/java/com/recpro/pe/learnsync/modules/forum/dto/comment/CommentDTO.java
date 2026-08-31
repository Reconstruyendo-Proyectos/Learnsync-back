package com.recpro.pe.learnsync.modules.forum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Integer idComment;
    private String message;
    private LocalDateTime creationDate;
    private String username;
    private String profilePhoto;
}
