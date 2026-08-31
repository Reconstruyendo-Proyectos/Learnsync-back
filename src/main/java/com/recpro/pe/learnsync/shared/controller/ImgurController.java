package com.recpro.pe.learnsync.shared.controller;

import com.recpro.pe.learnsync.shared.dto.image.ImageResponseDTO;
import com.recpro.pe.learnsync.shared.service.ImgurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Images")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/images")
public class ImgurController {
    private final ImgurService imgurService;

    @Operation(summary = "Subir imagen a Imgur", description = "POST multipart file, retorna {link, deleteHash} 201")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    public ResponseEntity<ImageResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imgurService.uploadImage(file));
    }

    @Operation(summary = "Eliminar imagen Imgur", description = "DELETE por deleteHash")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{deleteHash}")
    public ResponseEntity<Void> deleteFile(@PathVariable String deleteHash) {
        imgurService.deleteImage(deleteHash);
        return ResponseEntity.noContent().build();
    }
}
