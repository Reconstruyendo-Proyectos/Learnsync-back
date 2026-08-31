package com.recpro.pe.learnsync.modules.auth.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_token")
    @EqualsAndHashCode.Include
    private Integer idToken;
    @Column(name="token", nullable = false, unique = true)
    private String token;
    @Column(name="expiration_date", nullable = false, updatable = false)
    private LocalDateTime expirationDate;
    @Column(name="activation_date")
    private LocalDateTime activationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id_user")
    private User user;

    @PrePersist
    void prePersist() {
        if (expirationDate == null) expirationDate = LocalDateTime.now().plusMinutes(10);
    }

    public ConfirmationToken(Integer idToken, String token, LocalDateTime activationDate, User user) {
        this.idToken = idToken;
        this.token = token;
        this.expirationDate = LocalDateTime.now().plusMinutes(10);
        this.activationDate = activationDate;
        this.user = user;
    }

    public ConfirmationToken(Integer idToken, String token, LocalDateTime expirationDate, LocalDateTime activationDate, User user) {
        this.idToken = idToken;
        this.token = token;
        this.expirationDate = expirationDate != null ? expirationDate : LocalDateTime.now().plusMinutes(10);
        this.activationDate = activationDate;
        this.user = user;
    }
}
