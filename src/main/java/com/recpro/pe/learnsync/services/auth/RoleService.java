package com.recpro.pe.learnsync.services.auth;

import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.auth.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired private RoleRepository roleRepository;

    public Role getRole(String roleName) {
        ERole enumRole = transformStringtoERole(roleName);
        Optional<Role> role = roleRepository.findByRole(enumRole);
        if(role.isEmpty()) {
            throw new ResourceNotExistsException("El rol no existe");
        }
        return role.get();
    }

    private ERole transformStringtoERole(String roleName) {
        try {
            return ERole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotExistsException("El rol no existe");
        }
    }

}
