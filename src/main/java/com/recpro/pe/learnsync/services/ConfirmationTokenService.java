package com.recpro.pe.learnsync.services;

import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.ConfirmationToken;
import com.recpro.pe.learnsync.models.User;
import com.recpro.pe.learnsync.repos.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConfirmationTokenService {

    @Autowired private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired private EmailService emailService;

    public void sendEmail(User user) { //Cambiar URL a la del back desplegado
        String token = generateToken(user);
        String url = "http://localhost:8080/autenticacion/confirmation-token/"+token; //modificar puerto
        String mensaje = "Felicidades "+user.getUsername()+" por registrar su cuenta, estas a un solo paso de poder hacer uso " +
                "de las funciones de Learnsync, entra a este link para que puedas registrate," +url;
        emailService.sendEmail(user.getEmail(), "Activacion de cuenta", mensaje);
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
