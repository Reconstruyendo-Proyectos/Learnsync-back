package com.recpro.pe.learnsync.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String email;
    private LocalDateTime creationDate;
    private int points;
}
