package com.recpro.pe.learnsync.controllers.auth;

import com.recpro.pe.learnsync.dtos.auth.user.ImageUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@CrossOrigin("http://localhost:4200")
public class UserController {
    @Autowired private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getAuthenticatedUser() {
        return new ResponseEntity<>(User.toDto(userService.getAuthenticatedUser()), HttpStatus.OK);
    }

    @PatchMapping("/photo")
    public ResponseEntity<Void> uploadProfilePhoto(@RequestBody ImageUserDTO request) {
        return new ResponseEntity<>(userService.uploadProfilePhoto(request), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/photo")
    public ResponseEntity<Void> deleteProfilePhoto() {
        return new ResponseEntity<>(userService.deleteProfilePhoto(), HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/username")
    public ResponseEntity<Void> uploadProfileUsername(@RequestParam String username) {
        return new ResponseEntity<>(userService.uploadUsername(username), HttpStatus.NO_CONTENT);
    }
}
