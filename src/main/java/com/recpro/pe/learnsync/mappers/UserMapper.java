package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.auth.role.RoleDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired private RoleMapper roleMapper;

    public UserDTO toDto(User user) {
        RoleDTO role = roleMapper.toDto(user.getRole());
        return new UserDTO(user.getUsername(), user.getEmail(), user.getCreationDate(), user.getPoints(), role);
    }
}
