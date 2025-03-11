package com.recpro.pe.learnsync.dtos.prizes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrizeDTO {
    private Integer idPrize;
    private String name;
    private String description;
    private Integer price;
    private String image;
}
