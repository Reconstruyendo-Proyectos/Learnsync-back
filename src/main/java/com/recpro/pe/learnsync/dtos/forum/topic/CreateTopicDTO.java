package com.recpro.pe.learnsync.dtos.forum.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateTopicDTO {
    @NotEmpty(message = "Dato vacio")
    @NotBlank(message = "No es valido un dato con solo espacio en blanco")
    @Size(max = 50 , message = "Los topicos deben tener un maximo de 50 caracteres")
    private String name;
    @NotEmpty(message = "Dato vacio")
    @NotBlank(message = "No es valido un dato con solo espacio en blanco")
    @Size(max = 50, message = "La descripción debe tener un maximo de 50 caracteres")
    private String description;
    @NotNull(message = "No es valido un dato nulo")
    @NotEmpty(message = "Dato vacio")
    @NotBlank(message = "No es valido un dato con solo espacio en blanco")
    private String categoryName;
}
