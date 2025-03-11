package com.recpro.pe.learnsync.services.prizes;

import com.recpro.pe.learnsync.dtos.prizes.CreatePrizeDTO;
import com.recpro.pe.learnsync.dtos.prizes.PrizeDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Prize;
import com.recpro.pe.learnsync.repos.prizes.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrizeService {
    @Autowired private PrizeRepository prizeRepository;

    public List<PrizeDTO> listPrizes(Pageable pageable) {
        return prizeRepository.findAll(pageable).stream().map(Prize::toDto).toList();
    }

    public PrizeDTO createPrize(CreatePrizeDTO request) {
        Prize prize = new Prize(null, request.getName(), request.getDescription(), request.getPrice(), request.getImage(), new ArrayList<>());
        return Prize.toDto(prizeRepository.save(prize));
    }

    public PrizeDTO updatePrize(CreatePrizeDTO request, int idPrize) {
        Prize prize = getPrize(idPrize);
        prize.setName(request.getName());
        prize.setDescription(request.getDescription());
        prize.setPrice(request.getPrice());
        prize.setImage(request.getImage());
        return Prize.toDto(prizeRepository.save(prize));
    }

    public Void deletePrize(int idPrize) {
        prizeRepository.deleteById(idPrize);
        return null;
    }

    private Prize getPrize(int idPrize) {
        return prizeRepository.findById(idPrize).orElseThrow(() -> new ResourceNotExistsException("El premio con ID " + idPrize + " no existe"));
    }
}
