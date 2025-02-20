package com.recpro.pe.learnsync.controllers.auth;

import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthResponseDTO;
import com.recpro.pe.learnsync.dtos.auth.user.CreateUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.services.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/register/")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserDTO request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @GetMapping("/confirmation-token/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
        return new ResponseEntity<>(authService.activateAccount(token), HttpStatus.OK);
    }

    @PostMapping("/login/")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    @GetMapping("/user-by-token/")
    public ResponseEntity<AuthResponseDTO> getUserByToken(@RequestParam String token) {
        return new ResponseEntity<>(authService.getUserByToken(token), HttpStatus.OK);
    }
}
