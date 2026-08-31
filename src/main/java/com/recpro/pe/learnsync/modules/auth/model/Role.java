package com.recpro.pe.learnsync.modules.auth.model;

import com.recpro.pe.learnsync.modules.auth.dto.role.RoleDTO;
import com.recpro.pe.learnsync.shared.exception.ResourceNotExistsException;
import com.recpro.pe.learnsync.modules.auth.model.enums.ERole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    @EqualsAndHashCode.Include
    private Integer idRole;

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> users;

    public Role(Integer idRole, ERole roleName, List<User> users) {
        this.idRole = idRole;
        this.roleName = roleName;
        this.users = users;
    }

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
