package com.recpro.pe.learnsync.modules.auth.service;

import com.recpro.pe.learnsync.modules.auth.dto.user.BanUserDTO;
import com.recpro.pe.learnsync.modules.auth.dto.user.ImageUserDTO;
import com.recpro.pe.learnsync.modules.auth.dto.user.UserDTO;
import com.recpro.pe.learnsync.shared.exception.ResourceNotExistsException;
import com.recpro.pe.learnsync.modules.auth.mapper.UserMapper;
import com.recpro.pe.learnsync.modules.auth.model.User;
import com.recpro.pe.learnsync.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream().map(userMapper::toDto).toList();
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
        return userMapper.toDto(user);
    }

    public UserDTO unbanUser(String username) {
        User user = findByUser(username);
        user.setBanned(false);
        user.setBanDate(null);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return findByUser(username);
    }

    public void uploadProfilePhoto(ImageUserDTO request) {
        User user = getAuthenticatedUser();
        user.setProfilePhoto(request.getImage());
        userRepository.save(user);
    }


    public void deleteProfilePhoto() {
        User user = getAuthenticatedUser();
        user.setProfilePhoto(null);
        userRepository.save(user);
    }

    public void uploadUsername(String username) {
        User user = getAuthenticatedUser();
        user.setUsername(username);
        userRepository.save(user);
    }
}
