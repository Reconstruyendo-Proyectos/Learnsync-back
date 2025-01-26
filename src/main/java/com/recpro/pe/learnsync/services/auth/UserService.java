package com.recpro.pe.learnsync.services.auth;

import com.recpro.pe.learnsync.dtos.auth.user.BanUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public List<UserDTO> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream().map(User::toDto).toList();
    }

    public User findByUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotExistsException("El usuario "+ username + " no fue encontrado"));
    }

    public UserDTO banUser(BanUserDTO request) {
        User user = findByUser(request.getUsername());
        user.setBanned(true);
        user.setBanDate(request.getBanDate());
        userRepository.save(user);
        return User.toDto(user);
    }

    public UserDTO unbanUser(String username) {
        User user = findByUser(username);
        user.setBanned(false);
        user.setBanDate(null);
        userRepository.save(user);
        return User.toDto(user);
    }
}
