package com.recpro.pe.learnsync.units.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthResponseDTO;
import com.recpro.pe.learnsync.dtos.auth.user.CreateUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.exceptions.ConfigMailException;
import com.recpro.pe.learnsync.exceptions.EmailConfirmedException;
import com.recpro.pe.learnsync.exceptions.ExpiredTokenException;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.models.ConfirmationToken;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.auth.UserRepository;
import com.recpro.pe.learnsync.services.auth.AuthService;
import com.recpro.pe.learnsync.services.auth.ConfirmationTokenService;
import com.recpro.pe.learnsync.services.auth.JwtDetailsService;
import com.recpro.pe.learnsync.services.auth.RoleService;
import com.recpro.pe.learnsync.utils.JwtUtils;
import com.recpro.pe.learnsync.utils.UserSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private AuthService authService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtils jwtUtils;
    @Mock private JwtDetailsService userDetailsService;
    @Mock private ConfirmationTokenService confirmationTokenService;
    @Mock private RoleService roleService;
    @Mock private SpringTemplateEngine templateEngine;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(1, ERole.STUDENT, new ArrayList<>());
    }

    @Test
    void testRegister() {
        // Given
        CreateUserDTO request = new CreateUserDTO("username", "password", "email@example.com");
        User user = new User(null, request.getUsername(), request.getEmail(), "$2a$10$C6wCl1T//l1uD9rOgbWV..SNN3puoSw9n.iEfHIMMrZmelmN5Ivya", false, false, null, 0, new ArrayList<>(), new ArrayList<>(), role, null);

        // When
        when(roleService.getRole("STUDENT")).thenReturn(role);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("$2a$10$C6wCl1T//l1uD9rOgbWV..SNN3puoSw9n.iEfHIMMrZmelmN5Ivya");
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = authService.register(request);

        // Then
        verify(userRepository).save(any(User.class));
        verify(confirmationTokenService).sendEmail(any(User.class));
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("username");
        assertThat(result.getEmail()).isEqualTo("email@example.com");
    }

    @Test
    void testRegisterWhenUsernameExists() {
        // Given
        CreateUserDTO request = new CreateUserDTO("EXIST_USERNAME", "password", "email@example.com");

        // When
        when(roleService.getRole("STUDENT")).thenReturn(role);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(request));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El usuario EXIST_USERNAME existe");
    }

    @Test
    void testRegisterWhenEmailExists() {
        // Given
        CreateUserDTO request = new CreateUserDTO("username", "password", "email_exists@example.com");

        // When
        when(roleService.getRole("STUDENT")).thenReturn(role);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(request));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El email ya ha sido usado para la creación de otro usuario");
    }

    @Test
    void testRegisterWhenHaveAnEmailException() {
        // Given
        CreateUserDTO request = new CreateUserDTO("username", "password", "email@example.com");

        // When
        when(roleService.getRole("STUDENT")).thenReturn(role);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        doThrow(new ConfigMailException("Problemas al configurar el correo")).when(confirmationTokenService).sendEmail(any(User.class));

        ConfigMailException ex = assertThrows(ConfigMailException.class, () -> authService.register(request));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Problemas al configurar el correo");
    }

    @Test
    void testActivateAccount() {
        // Given
        String token = "validToken";
        User user = new User();
        user.setEnable(false);
        ConfirmationToken confirmationToken = new ConfirmationToken(1, token, null, user);
        String html = "<html><body>Test</body></html>";

        // When
        when(confirmationTokenService.findToken(token)).thenReturn(confirmationToken);
        when(templateEngine.process(eq("account-activated-template"), any(Context.class))).thenReturn(html);

        String result = authService.activateAccount(token);

        // Then
        verify(confirmationTokenService).saveChanges(any(ConfirmationToken.class));
        verify(userRepository).save(any(User.class));
        assertThat(result).isNotNull();
    }

    @Test
    void testActivateAccountWhenEmailHasUsed() {
        // Given
        String token = "validToken";
        User user = new User();
        user.setEnable(false);
        ConfirmationToken confirmationToken = new ConfirmationToken(1, token, LocalDateTime.now().minusMinutes(10), user);

        // When
        when(confirmationTokenService.findToken(token)).thenReturn(confirmationToken);

        EmailConfirmedException ex = assertThrows(EmailConfirmedException.class, () -> authService.activateAccount(token));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Este email ya fue confirmado");
    }

    @Test
    void testActivateAccountWhenTokenExpire() {
        // Given
        String token = "expiredToken";
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);

        // When
        when(confirmationTokenService.findToken(token)).thenReturn(confirmationToken);
        when(confirmationToken.getExpirationDate()).thenReturn(LocalDateTime.now().minusDays(1));
        when(confirmationToken.getActivationDate()).thenReturn(null);

        ExpiredTokenException ex = assertThrows(ExpiredTokenException.class, () -> authService.activateAccount(token));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Token expirado");
    }

    @Test
    void testAuthenticate() {
        // Given
        String username = "username";
        String password = "password";
        UserDetails userDetails = mock(UserDetails.class);

        // When
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("$2a$10$C6wCl1T//l1uD9rOgbWV..SNN3puoSw9n.iEfHIMMrZmelmN5Ivya");
        when(passwordEncoder.matches(password, "$2a$10$C6wCl1T//l1uD9rOgbWV..SNN3puoSw9n.iEfHIMMrZmelmN5Ivya")).thenReturn(true);

        // Then
        Authentication result = authService.authenticate(username, password);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("username");
        assertThat(result.getCredentials()).isEqualTo("$2a$10$C6wCl1T//l1uD9rOgbWV..SNN3puoSw9n.iEfHIMMrZmelmN5Ivya");
        assertThat(result.getAuthorities()).isEmpty();
    }

    @Test
    void testAuthenticateWhenUserDetailsIsNull() {
        // Given
        String username = "username";
        String password = "password";

        // When
        when(userDetailsService.loadUserByUsername(username)).thenReturn(null);

        BadCredentialsException ex = assertThrows(BadCredentialsException.class, () -> authService.authenticate(username, password));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Usuario o contraseña inválida");
    }

    @Test
    void testAuthenticateWhenPasswordNotMatches() {
        // Given
        String username = "username";
        String password = "password";
        UserDetails userDetails = new UserSecurity(username, "real-password", Collections.singleton(new SimpleGrantedAuthority("ROLE_")), new User());

        // When
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        BadCredentialsException ex = assertThrows(BadCredentialsException.class, () -> authService.authenticate(username, password));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Contraseña inválida");
    }

    @Test
    void testLogin() {
        // Given
        AuthRequestDTO request = new AuthRequestDTO("username", "password");
        UserDetails userDetails = mock(UserDetails.class);
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        Claim claim = mock(Claim.class);

        // When
        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);
        when(passwordEncoder.matches("password", userDetails.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(any(Authentication.class))).thenReturn("accessToken");
        when(jwtUtils.validateJWT("accessToken")).thenReturn(decodedJWT);
        when(jwtUtils.extractSpecificClaim(any(DecodedJWT.class), eq("authorities"))).thenReturn(claim);
        when(claim.asString()).thenReturn("ROLE_STUDENT");

        AuthResponseDTO result = authService.login(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo("username");
        assertThat(result.getRole()).isEqualTo("ROLE_STUDENT");
        assertThat(result.getToken()).isEqualTo("accessToken");
    }

    @Test
    void testLoginWhenJwtIsNotValid() {
        // Given
        AuthRequestDTO request = new AuthRequestDTO("username", "password");
        UserDetails userDetails = mock(UserDetails.class);

        // When
        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);
        when(passwordEncoder.matches("password", userDetails.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(any(Authentication.class))).thenReturn("accessToken");
        doThrow(new JWTVerificationException("Token inválido, no estás autorizado")).when(jwtUtils).validateJWT(anyString());

        JWTVerificationException ex = assertThrows(JWTVerificationException.class, () -> authService.login(request));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Token inválido, no estás autorizado");
    }
}
