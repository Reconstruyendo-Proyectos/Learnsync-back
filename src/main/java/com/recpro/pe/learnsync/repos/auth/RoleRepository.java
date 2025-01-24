package com.recpro.pe.learnsync.repos.auth;

import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(ERole roleName);
}
