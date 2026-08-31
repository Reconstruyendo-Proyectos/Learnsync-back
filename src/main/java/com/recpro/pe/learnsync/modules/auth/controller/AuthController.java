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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Registrar usuario", description = "Crea usuario STUDENT, envía email de activación. Requiere username, email y password >=8.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validación fallida"),
            @ApiResponse(responseCode = "409", description = "Username o email ya existe")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Activar cuenta", description = "Activa usuario vía token enviado por email. Retorna HTML de confirmación.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta activada"),
            @ApiResponse(responseCode = "404", description = "Token no válido"),
            @ApiResponse(responseCode = "409", description = "Token expirado o ya confirmado")
    })
    @GetMapping("/confirmation-token/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
        return ResponseEntity.ok(authService.activateAccount(token));
    }

    @Operation(summary = "Login con username/password", description = "Autentica y retorna JWT Bearer 30min. RateLimit 5/min por IP:username.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "JWT generado", content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas, cuenta no activada o baneada"),
            @ApiResponse(responseCode = "429", description = "Demasiados intentos")
    })
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
