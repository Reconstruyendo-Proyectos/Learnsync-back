package com.recpro.pe.learnsync.controllers.integrations;

import com.recpro.pe.learnsync.dtos.image.ImageResponseDTO;
import com.recpro.pe.learnsync.services.integrations.ImgurService;
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

    @PostMapping("")
    public ResponseEntity<ImageResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imgurService.uploadImage(file));
    }

    @DeleteMapping("/{deleteHash}")
    public ResponseEntity<Void> deleteFile(@PathVariable String deleteHash) {
        imgurService.deleteImage(deleteHash);
        return ResponseEntity.noContent().build();
    }
}
