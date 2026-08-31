package com.recpro.pe.learnsync.modules.gamification.controller;

import com.recpro.pe.learnsync.modules.gamification.dto.CreatePrizeDTO;
import com.recpro.pe.learnsync.modules.gamification.dto.PrizeDTO;
import com.recpro.pe.learnsync.modules.gamification.dto.PrizeToExchangeDTO;
import com.recpro.pe.learnsync.modules.gamification.service.PrizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Listar premios", description = "GET paginado")
    @GetMapping("")
    public ResponseEntity<List<PrizeDTO>> listPrizes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0) page = 0;
        size = Math.min(Math.max(size, 1), 50);
        return new ResponseEntity<>(prizeService.listPrizes(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Operation(summary = "Crear premio", description = "Requiere JWT ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    public ResponseEntity<PrizeDTO> createPrize(@RequestBody CreatePrizeDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prizeService.createPrize(request));
    }

    @Operation(summary = "Actualizar premio")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<PrizeDTO> updatePrize(@RequestBody CreatePrizeDTO request, @PathVariable int id) {
        return ResponseEntity.ok(prizeService.updatePrize(request, id));
    }

    @Operation(summary = "Eliminar premio")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrize(@PathVariable int id) {
        prizeService.deletePrize(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Canjear premio", description = "Descuenta points, 409 si ya canjeado, 400 si puntos insuficientes")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/exchange/{id}")
    public ResponseEntity<PrizeToExchangeDTO> exchangePrize(@PathVariable int id) {
        return ResponseEntity.ok(prizeService.redeemPrize(id));
    }
}
