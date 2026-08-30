package com.recpro.pe.learnsync.services.auth;

import com.recpro.pe.learnsync.dtos.auth.user.BanUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.ImageUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDTO> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream().map(User::toDto).toList();
    }

    public User findByUser(String username) {
        String finalUsername = username.replace("-", " ");
        return userRepository.findByUsername(finalUsername).orElseThrow(() -> new ResourceNotExistsException("El usuario no fue encontrado"));
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

    public User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return findByUser(username);
    }

    public Void uploadProfilePhoto(ImageUserDTO request) {
        User user = getAuthenticatedUser();
        user.setProfilePhoto(request.getImage());
        userRepository.save(user);
        return null;
    }


    public Void deleteProfilePhoto() {
        User user = getAuthenticatedUser();
        user.setProfilePhoto(null);
        userRepository.save(user);
        return null;
    }

    public Void uploadUsername(String username) {
        User user = getAuthenticatedUser();
        user.setUsername(username);
        userRepository.save(user);
        return null;
    }
}
