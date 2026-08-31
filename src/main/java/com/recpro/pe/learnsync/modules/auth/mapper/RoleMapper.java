package com.recpro.pe.learnsync.modules.auth.mapper;

import com.recpro.pe.learnsync.modules.auth.dto.role.RoleDTO;
import com.recpro.pe.learnsync.modules.auth.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDTO toDto(Role role) {
        if (role == null) return null;
        return new RoleDTO(role.getRoleName().name());
    }
}
