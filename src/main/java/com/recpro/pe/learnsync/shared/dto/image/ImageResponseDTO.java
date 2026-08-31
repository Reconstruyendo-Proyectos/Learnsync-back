package com.recpro.pe.learnsync.shared.dto.image;

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