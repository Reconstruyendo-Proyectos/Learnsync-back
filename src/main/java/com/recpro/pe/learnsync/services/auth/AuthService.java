package com.recpro.pe.learnsync.services.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthResponseDTO;
import com.recpro.pe.learnsync.dtos.auth.user.CreateUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.exceptions.EmailConfirmedException;
import com.recpro.pe.learnsync.exceptions.ExpiredTokenException;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.models.ConfirmationToken;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.auth.UserRepository;
import com.recpro.pe.learnsync.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private JwtDetailsService userDetailsService;
    @Autowired private ConfirmationTokenService confirmationTokenService;
    @Autowired private RoleService roleService;
    @Autowired private SpringTemplateEngine templateEngine;

    public UserDTO register(CreateUserDTO request) {
        Role role = roleService.getRole("STUDENT");
        User user = new User(null, request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), false, false, null, 0, new ArrayList<>(), new ArrayList<>(), role, null);
        if(userRepository.existsByUsername(user.getUsername())){
            throw new ResourceAlreadyExistsException("El usuario "+user.getUsername()+" existe");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ResourceAlreadyExistsException("El email ya ha sido usado para la creación de otro usuario");
        }
        userRepository.save(user);
        confirmationTokenService.sendEmail(user);
        return User.toDto(user);
    }

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
        model.put("image", "http://localhost:8080/assets/logo.png");
        // Configurar el contexto de Thymeleaf con los datos del modelo
        Context context = new Context();
        context.setVariables(model);
        // Procesar la plantilla usando Thymeleaf
        return templateEngine.process("account-activated-template", context);
    }

    public AuthResponseDTO login(AuthRequestDTO request){
        Authentication authentication = authenticate(request.getUsername(), request.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateToken(authentication);

        DecodedJWT decodedJWT = jwtUtils.validateJWT(accessToken);

        String role = jwtUtils.extractSpecificClaim(decodedJWT, "authorities").asString();

        return new AuthResponseDTO(request.getUsername(), role, accessToken);
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Usuario o contraseña inválida");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Contraseña inválida");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

}