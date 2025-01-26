package com.recpro.pe.learnsync.units.auth;

import com.recpro.pe.learnsync.dtos.auth.email.Mail;
import com.recpro.pe.learnsync.services.auth.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock private JavaMailSender mailSender;
    @Mock private SpringTemplateEngine templateEngine;
    @InjectMocks private EmailService emailService;

    @Test
    void testCreateEmail() {
        // Given
        String to = "jluyoc1@upao.edu.pe";
        String from = "example@example.com";
        String subject = "Asunto";
        Map<String, Object> model = new HashMap<>();

        // When
        Mail result = emailService.createMail(to, subject, model, from);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSubject()).isEqualTo("Asunto");
        assertThat(result.getFrom()).isEqualTo("example@example.com");
        assertThat(result.getTo()).isEqualTo("jluyoc1@upao.edu.pe");
    }

    @Test
    void testSendEmail() {
        // Given
        MimeMessage mimeMessage = mock(MimeMessage.class);

        String to = "recipient@example.com";
        String subject = "Test Subject";
        String from = "sender@example.com";
        Map<String, Object> model = new HashMap<>();
        model.put("key", "value");

        Mail mail = emailService.createMail(to, subject, model, from);
        String templateName = "test-template";

        // When
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html><body>Test</body></html>");

        emailService.sendEmail(mail, templateName);

        // Then
        verify(mailSender).createMimeMessage();
        verify(templateEngine).process(eq(templateName), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }
}
