package com.recpro.pe.learnsync.units.auth;

import com.recpro.pe.learnsync.dtos.auth.user.BanUserDTO;
import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.auth.UserRepository;
import com.recpro.pe.learnsync.services.auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    private List<User> users;

    @BeforeEach
    void setUp() {

        Role adminRole = new Role(1, ERole.ADMIN, new ArrayList<>());
        Role studentRole = new Role(2, ERole.STUDENT, new ArrayList<>());

        users = List.of(
                new User(1, "jluyo", "jluyoc1@upao.edu.pe", "upao2025", true, false, null, 100, new ArrayList<>(), new ArrayList<>(), adminRole, null),
                new User(2, "user2", "user2@example.com", "password2",  true, false, null, 200, new ArrayList<>(), new ArrayList<>(), studentRole, null),
                new User(3, "user3", "user3@example.com", "password3", true, false, null, 150, new ArrayList<>(), new ArrayList<>(), adminRole, null),
                new User(4, "user4", "user4@example.com", "password4", true, false, null, 300, new ArrayList<>(), new ArrayList<>(), studentRole, null),
                new User(5, "user5", "user5@example.com", "password5", true, false, null, 250, new ArrayList<>(), new ArrayList<>(), adminRole, null)
        );
    }

    @Test
    void testlistUsers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(users.subList(0, 3), pageable, users.size());

        // When
        when(userRepository.findAll(pageable)).thenReturn(page);

        List<UserDTO> result = userService.listUsers(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getUsername()).isEqualTo("jluyo");
        assertThat(result.get(1).getEmail()).isEqualTo("user2@example.com");
        assertThat(result.getLast().getPoints()).isEqualTo(150);
    }

    @Test
    void testFindByUser() {
        // Given
        String username = "jluyo";
        User user = users.getFirst();

        // When
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        User result = userService.findByUser(username);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("jluyo");
        assertThat(result.getEmail()).isEqualTo("jluyoc1@upao.edu.pe");
        assertThat(result.getPoints()).isEqualTo(100);
    }

    @Test
    void testFindByUserWhenUserNotExists() {
        // Given
        String username = "USER_NOT_EXISTS";

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> userService.findByUser(username));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El usuario USER_NOT_EXISTS no fue encontrado");
    }

    @Test
    void testBanUser() {
        // Given
        LocalDateTime fecha = LocalDateTime.now();
        BanUserDTO banUser = new BanUserDTO("jluyo", fecha);
        User user = users.getFirst();

        // When
        when(userRepository.findByUsername(banUser.getUsername())).thenReturn(Optional.of(user));
        UserDTO result = userService.banUser(banUser);

        // Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getUsername()).isEqualTo("jluyo");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("jluyoc1@upao.edu.pe");
        assertThat(result.getBanDate()).isEqualTo(fecha);
    }

    @Test
    void testBanUserWhenUserNotExists() {
        // Given
        BanUserDTO banUser = new BanUserDTO("USER_NOT_EXISTS", LocalDateTime.now());

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> userService.banUser(banUser));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El usuario USER_NOT_EXISTS no fue encontrado");
    }

    @Test
    void testUnbanUser() {
        // Given
        String username = "jluyo";
        User user = users.getFirst();

        // When
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        UserDTO result = userService.unbanUser(username);

        // Then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getUsername()).isEqualTo("jluyo");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("jluyoc1@upao.edu.pe");
        assertThat(result.getBanDate()).isNull();
    }

    @Test
    void testUnbanUserWhenUserNotExists() {
        // Given
        String username = "USER_NOT_EXISTS";

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> userService.unbanUser(username));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El usuario USER_NOT_EXISTS no fue encontrado");
    }
}
