package com.recpro.pe.learnsync.dtos.forum.category;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class CategoryDTO {
    private Integer idCategory;
    private String name;
    private String description;
}
