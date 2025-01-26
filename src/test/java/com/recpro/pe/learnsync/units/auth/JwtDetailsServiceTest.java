package com.recpro.pe.learnsync.units.auth;

import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.auth.UserRepository;
import com.recpro.pe.learnsync.services.auth.JwtDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class JwtDetailsServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private JwtDetailsService jwtDetailsService;

    @Test
    void testLoadUserByUsername() {
        // Given
        String username = "jluyo";
        User user = new User(1, "jluyo", "jluyoc1@upao.edu.pe", "upao2025", true, false, null, 100, new ArrayList<>(), new ArrayList<>(), new Role(1, ERole.ADMIN, new ArrayList<>()), null);

        // When
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetails result = jwtDetailsService.loadUserByUsername(username);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("jluyo");
        assertThat(result.getPassword()).isEqualTo("upao2025");
        assertThat(result.isEnabled()).isEqualTo(true);
        assertThat(result.getAuthorities().toString()).isEqualTo("[ROLE_ADMIN]");
    }

    @Test
    void testLoadUserByUsernameWhenUserNotExists() {
        // Given
        String username = "USER_NOT_EXISTS";

        // When
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, () -> jwtDetailsService.loadUserByUsername(username));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El usuario USER_NOT_EXISTS no fue encontrado");
    }
}
