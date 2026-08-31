package com.recpro.pe.learnsync.modules.auth.dto.email;

public record EmailDTO(String toUser,
                       String subject,
                       String message){
}