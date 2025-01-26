package com.recpro.pe.learnsync.units.auth;

import com.recpro.pe.learnsync.dtos.auth.email.Mail;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.ConfirmationToken;
import com.recpro.pe.learnsync.models.Role;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.auth.ConfirmationTokenRepository;
import com.recpro.pe.learnsync.services.auth.ConfirmationTokenService;
import com.recpro.pe.learnsync.services.auth.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTest {
    @Mock private ConfirmationTokenRepository confirmationTokenRepository;
    @InjectMocks private ConfirmationTokenService confirmationTokenService;
    @Mock private EmailService emailService;

    private ConfirmationToken confirmationToken;
    private User user;

    private String mailFrom;

    @BeforeEach
    void setUp() {
        user = new User(1, "jluyo", "jluyoc1@upao.edu.pe", "upao2025", true, false, null, 100, new ArrayList<>(), new ArrayList<>(), new Role(1, ERole.ADMIN, new ArrayList<>()), null);
        confirmationToken = new ConfirmationToken(1, "Token", null, user);
        mailFrom = "eprueba736@gmail.com";
    }

    @Test
    void testSaveChanges() {
        // When
        confirmationTokenService.saveChanges(confirmationToken);

        // Then
        verify(confirmationTokenRepository).saveAndFlush(confirmationToken);
    }

    @Test
    void testFindToken() {
        // Given
        String token = "Token";

        // When
        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.of(confirmationToken));
        ConfirmationToken result = confirmationTokenService.findToken(token);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdToken()).isEqualTo(1);
        assertThat(result.getToken()).isEqualTo("Token");
        assertThat(result.getActivationDate()).isNull();
    }

    @Test
    void testFindTokenWhenTokenNotExists() {
        // Given
        String token = "TOKEN_NOT_EXISTS";

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> confirmationTokenService.findToken(token));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Token no válido");
    }

    @Test
    void testGenerateToken() {
        // Given
        String token = UUID.randomUUID().toString();
        ConfirmationToken mockToken = new ConfirmationToken(2, token, null, user);

        // When
        when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(mockToken);
        String result = confirmationTokenService.generateToken(user);

        // Then
        ArgumentCaptor<ConfirmationToken> tokenArgumentCaptor = ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(confirmationTokenRepository).save(tokenArgumentCaptor.capture());
        assertThat(tokenArgumentCaptor.getValue()).isNotNull();

        assertThat(result).isEqualTo(token);
    }

    @Test
    void testSendEmail() {
        // Given
        Map<String, Object> expectedModel = new HashMap<>();
        String expectedUrl = "http://localhost:8080/auth/confirmation-token/" + confirmationToken.getToken();
        String expectedImage = "http://localhost:8080/assets/logo.png";
        expectedModel.put("user", user.getUsername());
        expectedModel.put("url", expectedUrl);
        expectedModel.put("image", expectedImage);

        Mail mail = new Mail(mailFrom, user.getEmail(), "Activa tu cuenta", expectedModel);

        // When
        when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(confirmationToken);
        when(emailService.createMail(anyString(), anyString(), anyMap(), anyString())).thenReturn(mail);
        confirmationTokenService.sendEmail(user);

        // Then
        verify(confirmationTokenRepository).save(any(ConfirmationToken.class));
        verify(emailService).createMail(eq(user.getEmail()), eq("Activa tu cuenta"), eq(expectedModel), eq(mailFrom));
        verify(emailService).sendEmail(any(Mail.class), eq("email/activate-user-email-template"));
    }
}
