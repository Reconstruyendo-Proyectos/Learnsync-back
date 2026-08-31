package com.recpro.pe.learnsync.modules.auth.dto.user;

import com.recpro.pe.learnsync.modules.auth.dto.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private LocalDateTime creationDate;
    private LocalDateTime banDate;
    private int points;
    private String profilePhoto;
    private RoleDTO role;
}
