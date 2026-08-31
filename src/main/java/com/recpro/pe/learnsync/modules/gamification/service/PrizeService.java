package com.recpro.pe.learnsync.modules.gamification.service;

import com.recpro.pe.learnsync.modules.gamification.dto.CreatePrizeDTO;
import com.recpro.pe.learnsync.modules.gamification.dto.PrizeDTO;
import com.recpro.pe.learnsync.modules.gamification.dto.PrizeToExchangeDTO;
import com.recpro.pe.learnsync.shared.exception.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.shared.exception.ResourceNotExistsException;
import com.recpro.pe.learnsync.modules.gamification.mapper.PrizeMapper;
import com.recpro.pe.learnsync.modules.gamification.model.Exchange;
import com.recpro.pe.learnsync.modules.gamification.model.Prize;
import com.recpro.pe.learnsync.modules.auth.model.User;
import com.recpro.pe.learnsync.modules.gamification.repository.ExchangeRepository;
import com.recpro.pe.learnsync.modules.gamification.repository.PrizeRepository;
import com.recpro.pe.learnsync.modules.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrizeService {
    private final PrizeRepository prizeRepository;
    private final UserService userService;
    private final ExchangeRepository exchangeRepository;
    private final PrizeMapper prizeMapper;

    public List<PrizeDTO> listPrizes(Pageable pageable) {
        return prizeRepository.findAll(pageable).stream().map(prizeMapper::toDto).toList();
    }

    public PrizeDTO createPrize(CreatePrizeDTO request) {
        Prize prize = new Prize(null, request.getName(), request.getDescription(), request.getPrice(), request.getImage(), new ArrayList<>());
        return prizeMapper.toDto(prizeRepository.save(prize));
    }

    public PrizeDTO updatePrize(CreatePrizeDTO request, int idPrize) {
        Prize prize = getPrize(idPrize);
        prize.setName(request.getName());
        prize.setDescription(request.getDescription());
        prize.setPrice(request.getPrice());
        prize.setImage(request.getImage());
        return prizeMapper.toDto(prizeRepository.save(prize));
    }

    public Void deletePrize(int idPrize) {
        prizeRepository.deleteById(idPrize);
        return null;
    }

    @Transactional
    public PrizeToExchangeDTO redeemPrize(Integer idPrize) {
        User user = userService.getAuthenticatedUser();
        Prize prize = getPrize(idPrize);

        if (user.getExchanges().stream().anyMatch(exchange -> exchange.getPrize().getIdPrize().equals(idPrize))) {
            throw new ResourceAlreadyExistsException("Este premio ya ha sido canjeado");
        }

        if (user.getPoints() < prize.getPrice()) {
            throw new IllegalArgumentException("Puntos insuficientes: tienes " + user.getPoints() + " necesitas " + prize.getPrice());
        }

        Exchange exchange = new Exchange();
        exchange.setUser(user);
        exchange.setPrize(prize);
        exchange.setRedemptionDate(LocalDateTime.now());

        user.setPoints(user.getPoints() - prize.getPrice());
        user.getExchanges().add(exchange);
        prize.getExchanges().add(exchange);
        exchangeRepository.save(exchange);
        return new PrizeToExchangeDTO(prize.getIdPrize(), prize.getName(), prize.getDescription(), prize.getPrice(), prize.getImage(), true, exchange.getRedemptionDate());
    }

    private Prize getPrize(int idPrize) {
        return prizeRepository.findById(idPrize).orElseThrow(() -> new ResourceNotExistsException("El premio con ID " + idPrize + " no existe"));
    }
}
