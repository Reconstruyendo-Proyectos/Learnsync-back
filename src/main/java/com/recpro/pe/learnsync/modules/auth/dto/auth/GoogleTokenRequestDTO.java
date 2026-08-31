package com.recpro.pe.learnsync.modules.auth.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleTokenRequestDTO {
    @NotBlank(message = "idToken no puede estar vacío")
    private String idToken;
}
