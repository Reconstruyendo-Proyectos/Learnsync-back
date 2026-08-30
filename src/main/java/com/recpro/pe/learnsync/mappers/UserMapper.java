package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserDTO toDto(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getCreationDate(),
                user.getBanDate(),
                user.getPoints(),
                user.getProfilePhoto(),
                roleMapper.toDto(user.getRole())
        );
    }
}
