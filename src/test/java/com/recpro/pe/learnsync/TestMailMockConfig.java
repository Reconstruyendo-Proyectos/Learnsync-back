package com.recpro.pe.learnsync;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@Configuration
@Profile("test")
public class TestMailMockConfig {
    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSender mock = Mockito.mock(JavaMailSender.class);
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(mock.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mock).send(any(MimeMessage.class));
        return mock;
    }
}
