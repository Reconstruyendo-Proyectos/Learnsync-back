package com.recpro.pe.learnsync.modules.auth.repository;

import com.recpro.pe.learnsync.modules.auth.model.Role;
import com.recpro.pe.learnsync.modules.auth.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(ERole roleName);
}
