package com.recpro.pe.learnsync.modules.gamification.model;

import com.recpro.pe.learnsync.modules.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "exchanges")
public class Exchange {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private ExchangeId id;

    @ManyToOne
    @MapsId("idUser")
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("idPrize")
    @JoinColumn(name = "id_prize", nullable = false)
    private Prize prize;

    @Column(name = "redemption_date")
    private LocalDateTime redemptionDate;
}

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
class ExchangeId implements Serializable {
    private Integer idUser;
    private Integer idPrize;
}
