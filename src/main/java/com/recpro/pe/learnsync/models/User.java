package com.recpro.pe.learnsync.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date creationDate;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "banned", nullable = false)
    private boolean banned;

    @Column(name = "ban_date")
    private Date banDate;

    @Column(name = "points", nullable = false)
    private int points;

    // Mapear 1 a Muchos con Comment

    // Mapear 1 a Muchos con Thread

    // Mapear 1 a Muchos con Maze

    // Mapear Muchos a 1 con Role
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", nullable = false, referencedColumnName = "id_role")
    private Role role;

    // Mapear 1 a 1 con ConfirmationToken
    @OneToOne(mappedBy = "user")
    private ConfirmationToken token;

    // Mapear Muchos a Uno con Prize
}
