package com.recpro.pe.learnsync.dtos.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ImageResponseDTO {
    private String url;
    private String deleteHash;
}