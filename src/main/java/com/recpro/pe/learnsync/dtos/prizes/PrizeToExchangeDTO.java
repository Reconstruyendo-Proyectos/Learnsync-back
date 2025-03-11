package com.recpro.pe.learnsync.dtos.prizes;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PrizeToExchangeDTO {
    private Integer idPrize;
    private String name;
    private String description;
    private Integer price;
    private String image;
    private Boolean isRedeemed;
    private LocalDateTime redemptionDate;
}
