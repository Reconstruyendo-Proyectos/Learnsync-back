package com.recpro.pe.learnsync.dtos.thread;

import com.recpro.pe.learnsync.dtos.user.UserDTO;
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
    private UserDTO user;
}
