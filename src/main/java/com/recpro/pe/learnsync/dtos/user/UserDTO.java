package com.recpro.pe.learnsync.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserDTO {

    private String username;
    private String email;
    private Date creationDate;
    private int points;
}
