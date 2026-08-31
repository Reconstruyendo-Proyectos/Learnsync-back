package com.recpro.pe.learnsync.modules.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.recpro.pe.learnsync.modules.auth.dto.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.modules.auth.dto.auth.AuthResponseDTO;
import com.recpro.pe.learnsync.modules.auth.dto.auth.GoogleLoginDTO;
import com.recpro.pe.learnsync.modules.auth.dto.user.CreateUserDTO;
import com.recpro.pe.learnsync.modules.auth.dto.user.UserDTO;
import com.recpro.pe.learnsync.shared.exception.EmailConfirmedException;
import com.recpro.pe.learnsync.shared.exception.ExpiredTokenException;
import com.recpro.pe.learnsync.shared.exception.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.modules.auth.mapper.UserMapper;
import com.recpro.pe.learnsync.modules.auth.model.ConfirmationToken;
import com.recpro.pe.learnsync.modules.auth.model.Role;
import com.recpro.pe.learnsync.modules.auth.model.User;
import com.recpro.pe.learnsync.modules.auth.repository.UserRepository;
import com.recpro.pe.learnsync.shared.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JwtDetailsService userDetailsService;
    private final ConfirmationTokenService confirmationTokenService;
    private final RoleService roleService;
    private final SpringTemplateEngine templateEngine;
    private final UserMapper userMapper;
    @Value("${app.backend.url}")
    private String backendUrl;

    @Transactional
    public UserDTO register(CreateUserDTO request) {
        Role role = roleService.getRole("STUDENT");
        User user = new User(null, request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), false, false, null, 0, null, new ArrayList<>(), new ArrayList<>(), role, null, new ArrayList<>());
        if(userRepository.existsByUsername(user.getUsername())){
            throw new ResourceAlreadyExistsException("El usuario "+user.getUsername()+" existe");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ResourceAlreadyExistsException("El email ya ha sido usado para la creación de otro usuario");
        }
        userRepository.save(user);
        confirmationTokenService.sendEmail(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public String activateAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findToken(token);
        if(confirmationToken.getActivationDate() != null) {
            throw new EmailConfirmedException("Este email ya fue confirmado");
        }
        LocalDateTime expirationDate = confirmationToken.getExpirationDate();
        if(expirationDate.isBefore(LocalDateTime.now())){
            throw new ExpiredTokenException("Token expirado");
        }
        confirmationToken.setActivationDate(LocalDateTime.now());
        confirmationTokenService.saveChanges(confirmationToken);
        User user = confirmationToken.getUser();
        user.setEnable(true);
        userRepository.save(user);
        Map<String, Object> model = new HashMap<>();
        model.put("image", backendUrl + "/assets/logo.png");
        Context context = new Context();
        context.setVariables(model);
        return templateEngine.process("account-activated-template", context);
    }

    public AuthResponseDTO login(AuthRequestDTO request){
        Authentication authentication = authenticate(request.getUsername(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateToken(authentication);
        jwtUtils.validateJWT(accessToken);
        return new AuthResponseDTO(accessToken);
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Usuario o contraseña inválida");
        }

        if (!userDetails.isEnabled()) {
            throw new BadCredentialsException("Cuenta no activada");
        }

        if (!userDetails.isAccountNonLocked()) {
            throw new BadCredentialsException("Cuenta baneada");
        }

        String storedPassword = userDetails.getPassword();
        if (storedPassword == null || !passwordEncoder.matches(password, storedPassword)){
            throw new BadCredentialsException("Contraseña inválida");
        }

        return new UsernamePasswordAuthenticationToken(username, storedPassword, userDetails.getAuthorities());
    }

    public AuthResponseDTO getUserByToken(String token) {
        Payload decodedJWT = jwtUtils.validateGoogleJWT(token);
        String username = jwtUtils.extractSpecificClaim(decodedJWT, "name").toString();
        String email = jwtUtils.extractSpecificClaim(decodedJWT, "email").toString();
        String profilePhoto = jwtUtils.extractSpecificClaim(decodedJWT, "picture").toString();
        GoogleLoginDTO googleLogin = new GoogleLoginDTO(username, email, profilePhoto);
        handleGoogleLogin(googleLogin);
        return new AuthResponseDTO(token);
    }

    private void handleGoogleLogin(GoogleLoginDTO request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()) {
            Role role = roleService.getRole("STUDENT");
            String dummyPassword = passwordEncoder.encode(UUID.randomUUID().toString());
            User newUser = new User(null, request.getUsername(), request.getEmail(), dummyPassword, true, false, null, 0, request.getProfilePhoto(), new ArrayList<>(), new ArrayList<>(), role, null, new ArrayList<>());
            userRepository.save(newUser);
        }
    }
}