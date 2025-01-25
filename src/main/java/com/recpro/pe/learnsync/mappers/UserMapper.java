package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.auth.role.RoleDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        RoleDTO role = Role.toDto(user.getRole());
        return new UserDTO(user.getUsername(), user.getEmail(), user.getCreationDate(), user.getPoints(), role);
    }
}
