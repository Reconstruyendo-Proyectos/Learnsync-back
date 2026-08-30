package com.recpro.pe.learnsync.controllers.auth;

import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthResponseDTO;
import com.recpro.pe.learnsync.dtos.auth.user.CreateUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @GetMapping("/confirmation-token/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
        return ResponseEntity.ok(authService.activateAccount(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
