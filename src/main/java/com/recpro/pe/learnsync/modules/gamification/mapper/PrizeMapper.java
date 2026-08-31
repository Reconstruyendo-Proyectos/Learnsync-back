package com.recpro.pe.learnsync.modules.gamification.mapper;

import com.recpro.pe.learnsync.modules.gamification.dto.PrizeDTO;
import com.recpro.pe.learnsync.modules.gamification.model.Prize;
import org.springframework.stereotype.Component;

@Component
public class PrizeMapper {

    public PrizeDTO toDto(Prize prize) {
        if (prize == null) return null;
        return new PrizeDTO(prize.getIdPrize(), prize.getName(), prize.getDescription(), prize.getPrice(), prize.getImage());
    }
}
