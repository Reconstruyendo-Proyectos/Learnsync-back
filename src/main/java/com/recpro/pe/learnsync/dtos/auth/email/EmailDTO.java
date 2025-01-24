package com.recpro.pe.learnsync.dtos.auth.email;

public record EmailDTO(String toUser,
                       String subject,
                       String message){
}