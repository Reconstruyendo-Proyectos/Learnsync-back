package com.recpro.pe.learnsync.dtos.auth.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleLoginDTO {
    @NotEmpty(message = "El usuario no puede estar vacio")
    @NotBlank(message = "El usuario no puede ser un espacio en blanco")
    private String username;
    @NotEmpty(message = "El email no puede estar vacio")
    @NotBlank(message = "El email no puede ser un espacio en blanco")
    @Email(message = "Debes enviar un correo válido")
    private String email;
    @NotEmpty(message = "La foto de perfil no puede estar vacia")
    @NotBlank(message = "La foto de perfil no puede ser un espacio en blanco")
    private String profilePhoto;
}
