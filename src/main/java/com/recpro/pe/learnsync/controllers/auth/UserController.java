package com.recpro.pe.learnsync.controllers.auth;

import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin("http://localhost:4200")
public class UserController {
    @Autowired private UserService userService;

    @GetMapping("/get-user/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        return new ResponseEntity<>(User.toDto(userService.findByUser(username)), HttpStatus.OK);
    }
}
