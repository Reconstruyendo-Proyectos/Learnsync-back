package com.recpro.pe.learnsync.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_token")
    private Integer idToken;
    @Column(name="token", nullable = false, unique = true)
    private String token;
    @Column(name="expiration_date", nullable = false)
    private final LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(10);
    @Column(name="activation_date")
    private LocalDateTime activationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;
}
