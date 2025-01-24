package com.recpro.pe.learnsync.services;

import com.recpro.pe.learnsync.dtos.user.UserDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream().map(this::returnUserDTO).toList();
    }

    public User findByUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotExistsException("El usuario "+ username + " no fue encontrado"));
    }

    public UserDTO banUser(String username, LocalDateTime banDate) {
        User user = findByUser(username);
        user.setBanned(true);
        user.setBanDate(banDate);
        userRepository.save(user);
        return returnUserDTO(user);
    }

    public UserDTO unbanUser(String username) {
        User user = findByUser(username);
        user.setBanned(false);
        user.setBanDate(null);
        userRepository.save(user);
        return returnUserDTO(user);
    }

    private UserDTO returnUserDTO(User user) {
        return new UserDTO(user.getUsername(), user.getEmail(), user.getCreationDate(), user.getPoints());
    }
}
