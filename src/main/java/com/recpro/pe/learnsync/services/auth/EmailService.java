package com.recpro.pe.learnsync.services.auth;

import com.recpro.pe.learnsync.dtos.auth.email.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailService {

    @Value("${email.sender}")
    private String emailUser;

    @Autowired private JavaMailSender mailSender;
    @Autowired private SpringTemplateEngine templateEngine;


    // ENVIAR MAIL SIN TEMPLATE
    public void sendEmail(String toUser, String subject, String message){

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailUser);
        mailMessage.setTo(toUser);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    // ENVIAR MAIL CON TEMPLATE

    public Mail createMail(String to, String subject, Map<String, Object> model, String from) {
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setModel(model);
        return mail;
    }

    public void sendEmail(Mail mail, String templateName) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        // Configurar el contexto de Thymeleaf con los datos del modelo
        Context context = new Context();
        context.setVariables(mail.getModel());

        // Procesar la plantilla usando Thymeleaf
        String html = templateEngine.process(templateName, context);
        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());

        // Si necesitas adjuntar un archivo
        //helper.addAttachment("MyTestFile.txt", new ByteArrayResource("test".getBytes()));
        InputStreamSource imageSource = new ClassPathResource("static/assets/logo.png");
        helper.addInline("logo", imageSource, "image/png");

        mailSender.send(message);

    }
}
