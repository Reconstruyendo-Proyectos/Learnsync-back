package com.recpro.pe.learnsync.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchanges")
public class Exchange {
    @EmbeddedId
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
@Data
@AllArgsConstructor
@NoArgsConstructor
class ExchangeId implements Serializable {
    private Integer idUser;
    private Integer idPrize;
}
