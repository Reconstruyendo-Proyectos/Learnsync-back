package com.recpro.pe.learnsync.services.auth;

import com.recpro.pe.learnsync.dtos.auth.email.Mail;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.ConfirmationToken;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.auth.ConfirmationTokenRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ConfirmationTokenService {

    @Autowired private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired private EmailService emailService;
    @Value("${email.sender}")
    private String mailFrom;

    public void sendEmail(User user) throws MessagingException { //Cambiar URL a la del back desplegado
        Map<String, Object> model = new HashMap<>();
        String token = generateToken(user);
        String url = "http://localhost:8080/auth/confirmation-token/"+token;
        String image = "http://localhost:8080/assets/logo.png";
        model.put("user", user.getUsername());
        model.put("url", url);
        model.put("image", image);
        Mail mail = emailService.createMail(user.getEmail(), "Activa tu cuenta", model, mailFrom);
        emailService.sendEmail(mail, "email/activate-user-email-template");
    }

    public ConfirmationToken findToken(String token){
        if(!confirmationTokenRepository.existsConfirmationTokenByToken(token)){
            throw new ResourceNotExistsException("Token no válido");
        }
        return confirmationTokenRepository.findByToken(token);
    }

    public void saveChanges(ConfirmationToken confirmationToken){confirmationTokenRepository.saveAndFlush(confirmationToken);}

    private String generateToken(User user){
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(null, token, null, user);
        return confirmationTokenRepository.save(confirmationToken).getToken();
    }
}
