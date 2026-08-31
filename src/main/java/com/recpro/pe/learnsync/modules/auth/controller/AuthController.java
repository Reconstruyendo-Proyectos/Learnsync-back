package com.recpro.pe.learnsync.modules.auth.controller;

import com.recpro.pe.learnsync.modules.auth.dto.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.modules.auth.dto.auth.AuthResponseDTO;
import com.recpro.pe.learnsync.modules.auth.dto.auth.GoogleTokenRequestDTO;
import com.recpro.pe.learnsync.modules.auth.dto.user.CreateUserDTO;
import com.recpro.pe.learnsync.modules.auth.dto.user.UserDTO;
import com.recpro.pe.learnsync.shared.exception.TooManyRequestsException;
import com.recpro.pe.learnsync.modules.auth.service.AuthService;
import com.recpro.pe.learnsync.shared.service.RateLimitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RateLimitService rateLimitService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @GetMapping("/confirmation-token/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
        return ResponseEntity.ok(authService.activateAccount(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request, HttpServletRequest httpRequest) {
        String key = httpRequest.getRemoteAddr() + ":" + request.getUsername();
        if (!rateLimitService.isAllowed(key)) {
            throw new TooManyRequestsException("Demasiados intentos, espera 60s");
        }
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Login con Google", description = "Valida idToken de Google, crea usuario si no existe y retorna JWT de la API")
    @PostMapping("/google")
    public ResponseEntity<AuthResponseDTO> googleLogin(@Valid @RequestBody GoogleTokenRequestDTO request, HttpServletRequest httpRequest) {
        String key = httpRequest.getRemoteAddr() + ":google";
        if (!rateLimitService.isAllowed(key)) {
            throw new TooManyRequestsException("Demasiados intentos, espera 60s");
        }
        return ResponseEntity.ok(authService.googleLogin(request));
    }
}
