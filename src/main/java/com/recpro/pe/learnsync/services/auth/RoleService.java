package com.recpro.pe.learnsync.services.auth;

import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.auth.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired private RoleRepository roleRepository;

    public Role getRole(String roleName) {
        ERole enumRole = Role.transformStringtoERole(roleName);
        return roleRepository.findByRoleName(enumRole).orElseThrow(() -> new ResourceNotExistsException("El rol no existe"));
    }

}
