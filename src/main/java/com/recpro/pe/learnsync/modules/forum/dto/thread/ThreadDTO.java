package com.recpro.pe.learnsync.modules.forum.dto.thread;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ThreadDTO {
    private Integer idThread;
    private String title;
    private String message;
    private LocalDateTime creationDate;
    private Integer likes;
    private Integer stars;
    private String file;
    private String username;
    private String profilePhoto;
    private String topicName;
    private Integer nroComments;
}
