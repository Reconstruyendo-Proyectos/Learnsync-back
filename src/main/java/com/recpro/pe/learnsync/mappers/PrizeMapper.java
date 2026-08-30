package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.prizes.PrizeDTO;
import com.recpro.pe.learnsync.models.Prize;
import org.springframework.stereotype.Component;

@Component
public class PrizeMapper {

    public PrizeDTO toDto(Prize prize) {
        if (prize == null) return null;
        return new PrizeDTO(prize.getIdPrize(), prize.getName(), prize.getDescription(), prize.getPrice(), prize.getImage());
    }
}
