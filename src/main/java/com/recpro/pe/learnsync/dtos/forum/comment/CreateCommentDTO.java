package com.recpro.pe.learnsync.dtos.forum.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCommentDTO {
    @NotEmpty(message = "Dato vacio")
    @Size(max=200, message = "El mensaje puede tener un máximo de 200 carácteres")
    @NotBlank(message = "No es valido un dato con solo espacio en blanco")
    private String message;
    @NotEmpty(message = "Dato vacio")
    @NotBlank(message = "No es valido un dato con solo espacio en blanco")
    private String username;
    @NotNull(message = "Dato vacio")
    private Integer idThread;
}
