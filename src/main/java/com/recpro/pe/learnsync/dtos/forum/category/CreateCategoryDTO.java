package com.recpro.pe.learnsync.dtos.forum.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCategoryDTO {
    @NotNull(message = "Dato no válido")
    @NotEmpty(message = "Dato vacio")
    @NotBlank(message = "No es valido un dato con solo espacio en blanco")
    @Size(max = 40 , message = "Las categorias tienen un maximo de 40 caracteres")
    private String name;
    @Size(max = 50 , message = "La descripción tiene un maximo de 50 caracteres")
    private String description;
}
