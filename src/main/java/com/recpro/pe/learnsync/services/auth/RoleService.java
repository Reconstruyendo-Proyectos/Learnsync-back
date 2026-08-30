package com.recpro.pe.learnsync.services.auth;

import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.auth.RoleRepository;
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
