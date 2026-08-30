package com.recpro.pe.learnsync.controllers.auth;

import com.recpro.pe.learnsync.dtos.auth.user.ImageUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.mappers.UserMapper;
import com.recpro.pe.learnsync.services.auth.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getAuthenticatedUser() {
        return ResponseEntity.ok(userMapper.toDto(userService.getAuthenticatedUser()));
    }

    @PatchMapping("/photo")
    public ResponseEntity<Void> uploadProfilePhoto(@RequestBody ImageUserDTO request) {
        userService.uploadProfilePhoto(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/photo")
    public ResponseEntity<Void> deleteProfilePhoto() {
        userService.deleteProfilePhoto();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/username")
    public ResponseEntity<Void> uploadProfileUsername(@RequestParam String username) {
        userService.uploadUsername(username);
        return ResponseEntity.noContent().build();
    }
}
