package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.auth.role.RoleDTO;
import com.recpro.pe.learnsync.models.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDTO toDto(Role role) {
        return new RoleDTO(role.getRoleName().name());
    }
}
