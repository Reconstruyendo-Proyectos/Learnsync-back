package com.recpro.pe.learnsync.controllers.prizes;

import com.recpro.pe.learnsync.dtos.prizes.CreatePrizeDTO;
import com.recpro.pe.learnsync.dtos.prizes.PrizeDTO;
import com.recpro.pe.learnsync.dtos.prizes.PrizeToExchangeDTO;
import com.recpro.pe.learnsync.services.prizes.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("prize")
public class PrizeController {
    @Autowired private PrizeService prizeService;

    @GetMapping("/list/")
    public ResponseEntity<List<PrizeDTO>> listPrizes(@RequestParam int page) {
        return new ResponseEntity<>(prizeService.listPrizes(PageRequest.of(page, 10)), HttpStatus.OK);
    }

    @PostMapping("/create/")
    public ResponseEntity<PrizeDTO> createPrize(@RequestBody CreatePrizeDTO request) {
        return new ResponseEntity<>(prizeService.createPrize(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PrizeDTO> updatePrize(@RequestBody CreatePrizeDTO request, @PathVariable int id) {
        return new ResponseEntity<>(prizeService.updatePrize(request, id), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePrize(@PathVariable int id) {
        return new ResponseEntity<>(prizeService.deletePrize(id), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/exchange-prize/{id}")
    public ResponseEntity<PrizeToExchangeDTO> exchangePrize(@PathVariable int id) {
        return new ResponseEntity<>(prizeService.redeemPrize(id), HttpStatus.OK);
    }
}
