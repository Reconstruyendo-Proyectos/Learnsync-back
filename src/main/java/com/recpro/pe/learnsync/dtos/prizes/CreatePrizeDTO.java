package com.recpro.pe.learnsync.dtos.prizes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePrizeDTO {
    @NotEmpty(message = "El nombre no puede estar vacío")
    @NotBlank(message = "El nombre no puede ser un espacio en blanco")
    private String name;
    @NotEmpty(message = "La descripcion no puede estar vacía")
    @NotBlank(message = "La descripcion no puede ser un espacio en blanco")
    private String description;
    @NotNull(message = "El precio no puede ser null")
    private Integer price;
    @NotEmpty(message = "La imagen no puede estar vacía")
    @NotBlank(message = "La imagen no puede ser un espacio en blanco")
    private String image;
}
