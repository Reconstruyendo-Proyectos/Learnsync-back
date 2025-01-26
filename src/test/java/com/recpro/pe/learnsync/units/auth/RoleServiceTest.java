package com.recpro.pe.learnsync.units.auth;

import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.auth.RoleRepository;
import com.recpro.pe.learnsync.services.auth.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testGetRole() {
        // Given
        String roleName = "STUDENT";
        Role role = new Role(1, ERole.STUDENT, new ArrayList<>());

        // When
        when(roleRepository.findByRoleName(Role.transformStringtoERole(roleName))).thenReturn(Optional.of(role));
        Role result = roleService.getRole(roleName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdRole()).isEqualTo(1);
        assertThat(result.getRoleName()).isEqualTo(ERole.STUDENT);
    }

    @Test
    void testGetTopicWhenTopicNotExists() {
        // Given
        String roleName = "ROLE_NOT_EXISTS";

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> roleService.getRole(roleName));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El rol no existe");
    }
}
