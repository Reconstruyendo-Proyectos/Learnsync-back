package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.auth.role.RoleDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "creation_date", nullable = false)
    private final LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "banned", nullable = false)
    private boolean banned;

    @Column(name = "ban_date")
    private LocalDateTime banDate;

    @Column(name = "points", nullable = false)
    private int points;

    // Mapear 1 a Muchos con Comment
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // Mapear 1 a Muchos con Thread
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Thread> threads;

    // Mapear 1 a Muchos con Maze

    // Mapear Muchos a 1 con Role
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", nullable = false, referencedColumnName = "id_role")
    private Role role;

    // Mapear 1 a 1 con ConfirmationToken
    @OneToOne(mappedBy = "user")
    private ConfirmationToken token;

    // Mapear Muchos a Uno con Prize

    public static UserDTO toDto(User user) {
        return new UserDTO(user.getUsername(), user.getEmail(), user.getCreationDate(), user.getPoints(), Role.toDto(user.getRole()));
    }
}
