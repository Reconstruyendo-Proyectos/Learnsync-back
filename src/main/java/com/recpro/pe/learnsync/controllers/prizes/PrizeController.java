package com.recpro.pe.learnsync.controllers.prizes;

import com.recpro.pe.learnsync.dtos.prizes.CreatePrizeDTO;
import com.recpro.pe.learnsync.dtos.prizes.PrizeDTO;
import com.recpro.pe.learnsync.dtos.prizes.PrizeToExchangeDTO;
import com.recpro.pe.learnsync.services.prizes.PrizeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Prizes")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/prizes")
public class PrizeController {
    private final PrizeService prizeService;

    @GetMapping("")
    public ResponseEntity<List<PrizeDTO>> listPrizes(@RequestParam(defaultValue = "0") int page) {
        if (page < 0) page = 0;
        return new ResponseEntity<>(prizeService.listPrizes(PageRequest.of(page, 10)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PrizeDTO> createPrize(@RequestBody CreatePrizeDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prizeService.createPrize(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrizeDTO> updatePrize(@RequestBody CreatePrizeDTO request, @PathVariable int id) {
        return ResponseEntity.ok(prizeService.updatePrize(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrize(@PathVariable int id) {
        prizeService.deletePrize(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/exchange/{id}")
    public ResponseEntity<PrizeToExchangeDTO> exchangePrize(@PathVariable int id) {
        return ResponseEntity.ok(prizeService.redeemPrize(id));
    }
}
