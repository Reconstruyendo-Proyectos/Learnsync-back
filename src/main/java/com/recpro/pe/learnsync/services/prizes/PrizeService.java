package com.recpro.pe.learnsync.services.prizes;

import com.recpro.pe.learnsync.dtos.prizes.CreatePrizeDTO;
import com.recpro.pe.learnsync.dtos.prizes.PrizeDTO;
import com.recpro.pe.learnsync.dtos.prizes.PrizeToExchangeDTO;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Exchange;
import com.recpro.pe.learnsync.models.Prize;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.prizes.ExchangeRepository;
import com.recpro.pe.learnsync.repos.prizes.PrizeRepository;
import com.recpro.pe.learnsync.services.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrizeService {
    private final PrizeRepository prizeRepository;
    private final UserService userService;
    private final ExchangeRepository exchangeRepository;

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

    public PrizeToExchangeDTO redeemPrize(Integer idPrize) {
        User user = userService.getAuthenticatedUser();
        Prize prize = getPrize(idPrize);

        if(user.getExchanges().stream().anyMatch(exchange -> exchange.getPrize().getIdPrize().equals(idPrize))) {
            throw new ResourceAlreadyExistsException("Este premio ya ha sido canjeado");
        }

        Exchange exchange = new Exchange();
        exchange.setUser(user);
        exchange.setPrize(prize);
        exchange.setRedemptionDate(LocalDateTime.now());

        user.getExchanges().add(exchange);
        prize.getExchanges().add(exchange);
        exchangeRepository.save(exchange);
        return new PrizeToExchangeDTO(prize.getIdPrize(), prize.getName(), prize.getDescription(), prize.getPrice(), prize.getImage(), true, exchange.getRedemptionDate());
    }

    private Prize getPrize(int idPrize) {
        return prizeRepository.findById(idPrize).orElseThrow(() -> new ResourceNotExistsException("El premio con ID " + idPrize + " no existe"));
    }
}
