package com.recpro.pe.learnsync.dtos.auth.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BanUserDTO {
    private String username;
    private LocalDateTime banDate;
}
