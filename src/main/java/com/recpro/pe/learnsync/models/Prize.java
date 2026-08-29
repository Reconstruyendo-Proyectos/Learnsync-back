package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.prizes.PrizeDTO;
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
@Table
@Entity(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prize")
    @EqualsAndHashCode.Include
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

    public Prize(Integer idPrize, String name, String description, Integer price, String image, List<Exchange> exchanges) {
        this.idPrize = idPrize;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.exchanges = exchanges;
    }

    public static PrizeDTO toDto(Prize prize) {
        return new PrizeDTO(prize.getIdPrize(), prize.getName(), prize.getDescription(), prize.getPrice(), prize.getImage());
    }
}
