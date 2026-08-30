package com.recpro.pe.learnsync.services.auth;

import com.recpro.pe.learnsync.dtos.auth.email.Mail;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.ConfirmationToken;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.auth.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {


    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;
    @Value("${email.sender}")
    private String mailFrom;
    @Value("${app.backend.url}")
    private String backendUrl;

    public void sendEmail(User user) {
        Map<String, Object> model = new HashMap<>();
        String token = generateToken(user);
        String url = backendUrl + "/api/v1/auth/confirmation-token/" + token;
        String image = backendUrl + "/assets/logo.png";
        model.put("user", user.getUsername());
        model.put("url", url);
        model.put("image", image);
        Mail mail = emailService.createMail(user.getEmail(), "Activa tu cuenta", model, mailFrom);
        emailService.sendEmail(mail, "email/activate-user-email-template");
    }

    public ConfirmationToken findToken(String token){
        return confirmationTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotExistsException("Token no válido"));
    }

    public void saveChanges(ConfirmationToken confirmationToken){confirmationTokenRepository.saveAndFlush(confirmationToken);}

    public String generateToken(User user){
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(null, token, null, user);
        return confirmationTokenRepository.save(confirmationToken).getToken();
    }
}
