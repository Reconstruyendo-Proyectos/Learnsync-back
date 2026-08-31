package com.recpro.pe.learnsync.modules.auth.service;

import com.recpro.pe.learnsync.shared.exception.ResourceNotExistsException;
import com.recpro.pe.learnsync.modules.auth.model.Role;
import com.recpro.pe.learnsync.modules.auth.model.enums.ERole;
import com.recpro.pe.learnsync.modules.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRole(String roleName) {
        ERole enumRole = Role.transformStringtoERole(roleName);
        return roleRepository.findByRoleName(enumRole).orElseThrow(() -> new ResourceNotExistsException("El rol no existe"));
    }

}
