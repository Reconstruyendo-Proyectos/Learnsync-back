package com.recpro.pe.learnsync.services.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthResponseDTO;
import com.recpro.pe.learnsync.dtos.auth.user.CreateUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.exceptions.EmailConfirmedException;
import com.recpro.pe.learnsync.exceptions.ExpiredTokenException;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.mappers.UserMapper;
import com.recpro.pe.learnsync.models.ConfirmationToken;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.auth.UserRepository;
import com.recpro.pe.learnsync.utils.JwtUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private JwtDetailsService userDetailsService;
    @Autowired private ConfirmationTokenService confirmationTokenService;
    @Autowired private UserMapper userMapper;
    @Autowired private RoleService roleService;

    public UserDTO register(CreateUserDTO request) throws MessagingException {
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
        return userMapper.toDto(user);
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
        return "account-activated-template";
    }

    public AuthResponseDTO login(AuthRequestDTO request){
        Authentication authentication = authenticate(request.getUsername(), request.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateToken(authentication);

        DecodedJWT decodedJWT = jwtUtils.validateJWT(accessToken);

        String role = jwtUtils.extractSpecificClaim(decodedJWT, "authorities").asString();

        return new AuthResponseDTO(request.getUsername(), role, accessToken);
    }

    private Authentication authenticate(String username, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid Username or Password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

}