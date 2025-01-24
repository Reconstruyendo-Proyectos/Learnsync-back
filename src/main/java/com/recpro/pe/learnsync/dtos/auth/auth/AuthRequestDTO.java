package com.recpro.pe.learnsync.dtos.auth.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDTO {
    @NotNull(message = "El username no puede ser null")
    @NotEmpty(message = "El username no puede estar vacio")
    @NotBlank(message = "El usuario no puede ser un espacio en blanco")
    String username;
    @NotNull(message = "La contraseña no puede ser null")
    @NotEmpty(message = "La contraseña no puede estar vacia")
    @NotBlank(message = "La contraseña no puede ser un espacio en blanco")
    String password;
}