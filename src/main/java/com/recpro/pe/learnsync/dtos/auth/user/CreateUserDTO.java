package com.recpro.pe.learnsync.dtos.auth.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserDTO {
    @NotEmpty(message = "Dato vacio")
    @NotBlank(message = "No es valido un dato con solo espacio en blanco")
    String username;
    @NotEmpty(message = "Dato vacio")
    @NotBlank(message = "No es valido un dato con solo espacio en blanco")
    @Size(min = 8, message = "La contraseña debe tener minimo 8 caracteres")
    String password;
    @Email(message = "Formato incorrecto")
    @NotEmpty(message = "Dato vacio")
    String email;
}
