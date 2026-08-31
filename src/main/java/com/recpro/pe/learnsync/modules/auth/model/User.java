package com.recpro.pe.learnsync.modules.auth.model;

import com.recpro.pe.learnsync.modules.auth.dto.user.UserDTO;
import com.recpro.pe.learnsync.modules.forum.model.Comment;
import com.recpro.pe.learnsync.modules.forum.model.Thread;
import com.recpro.pe.learnsync.modules.gamification.model.Exchange;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    @EqualsAndHashCode.Include
    private Integer idUser;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "banned", nullable = false)
    private boolean banned;

    @Column(name = "ban_date")
    private LocalDateTime banDate;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Thread> threads;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", nullable = false, referencedColumnName = "id_role")
    private Role role;

    @OneToOne(mappedBy = "user")
    private ConfirmationToken token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exchange> exchanges;

    @PrePersist
    void prePersist() {
        if (creationDate == null) creationDate = LocalDateTime.now();
    }

    public User(Integer idUser, String username, String email, String password, boolean enable, boolean banned, LocalDateTime banDate, int points, String profilePhoto, List<Comment> comments, List<Thread> threads, Role role, ConfirmationToken token, List<Exchange> exchanges) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.password = password;
        this.creationDate = LocalDateTime.now();
        this.enable = enable;
        this.banned = banned;
        this.banDate = banDate;
        this.points = points;
        this.profilePhoto = profilePhoto;
        this.comments = comments;
        this.threads = threads;
        this.role = role;
        this.token = token;
        this.exchanges = exchanges;
    }

    public User(Integer idUser, String username, String email, String password, LocalDateTime creationDate, boolean enable, boolean banned, LocalDateTime banDate, int points, String profilePhoto, List<Comment> comments, List<Thread> threads, Role role, ConfirmationToken token, List<Exchange> exchanges) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate != null ? creationDate : LocalDateTime.now();
        this.enable = enable;
        this.banned = banned;
        this.banDate = banDate;
        this.points = points;
        this.profilePhoto = profilePhoto;
        this.comments = comments;
        this.threads = threads;
        this.role = role;
        this.token = token;
        this.exchanges = exchanges;
    }

    public static UserDTO toDto(User user) {
        return new UserDTO(user.getUsername(), user.getEmail(), user.getCreationDate(), user.getBanDate(), user.getPoints(), user.getProfilePhoto(), Role.toDto(user.getRole()));
    }
}
