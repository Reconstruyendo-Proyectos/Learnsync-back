package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.prizes.PrizeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prize")
    private Integer idPrize;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "image", nullable = false)
    private String image;

    @OneToMany(mappedBy = "prize", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exchange> exchanges;

    public static PrizeDTO toDto(Prize prize) {
        return new PrizeDTO(prize.getIdPrize(), prize.getName(), prize.getDescription(), prize.getPrice(), prize.getImage());
    }
}
