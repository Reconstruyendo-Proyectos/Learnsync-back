package com.recpro.pe.learnsync.controllers.integrations;

import com.recpro.pe.learnsync.dtos.image.ImageResponseDTO;
import com.recpro.pe.learnsync.services.integrations.ImgurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/file")
public class ImgurController {
    @Autowired private ImgurService imgurService;

    @PostMapping("")
    public ResponseEntity<ImageResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(imgurService.uploadImage(file), HttpStatus.OK);
    }

    @DeleteMapping("/{deleteHash}")
    public ResponseEntity<Void> deleteFile(@PathVariable String deleteHash) {
        return new ResponseEntity<>(imgurService.deleteImage(deleteHash), HttpStatus.NO_CONTENT);
    }
}
