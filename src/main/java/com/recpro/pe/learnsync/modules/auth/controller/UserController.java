package com.recpro.pe.learnsync.modules.auth.controller;

import com.recpro.pe.learnsync.modules.auth.dto.user.ImageUserDTO;
import com.recpro.pe.learnsync.modules.auth.dto.user.UserDTO;
import com.recpro.pe.learnsync.modules.auth.mapper.UserMapper;
import com.recpro.pe.learnsync.modules.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Perfil autenticado", description = "Retorna DTO del usuario del JWT Bearer")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "401", description = "No autenticado")})
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getAuthenticatedUser() {
        return ResponseEntity.ok(userMapper.toDto(userService.getAuthenticatedUser()));
    }

    @Operation(summary = "Actualizar foto perfil", description = "PATCH con {image: url}")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Actualizado"), @ApiResponse(responseCode = "401", description = "No autenticado")})
    @PatchMapping("/photo")
    public ResponseEntity<Void> uploadProfilePhoto(@RequestBody ImageUserDTO request) {
        userService.uploadProfilePhoto(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar foto perfil")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/photo")
    public ResponseEntity<Void> deleteProfilePhoto() {
        userService.deleteProfilePhoto();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar username", description = "PATCH ?username=nuevo")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/username")
    public ResponseEntity<Void> uploadProfileUsername(@RequestParam String username) {
        userService.uploadUsername(username);
        return ResponseEntity.noContent().build();
    }
}
