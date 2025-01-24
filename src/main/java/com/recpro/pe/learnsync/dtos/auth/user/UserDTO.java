package com.recpro.pe.learnsync.dtos.auth.user;

import com.recpro.pe.learnsync.dtos.auth.role.RoleDTO;
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
    private RoleDTO role;
}
