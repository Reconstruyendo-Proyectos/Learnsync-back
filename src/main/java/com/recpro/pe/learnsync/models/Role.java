package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.auth.role.RoleDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer idRole;

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole roleName;

    // Mapear 1 a Muchos con User
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> users;

    public static RoleDTO toDto(Role role) {
        return new RoleDTO(role.getRoleName().name());
    }

    public static ERole transformStringtoERole(String roleName) {
        try {
            return ERole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotExistsException("El rol no existe");
        }
    }
}
