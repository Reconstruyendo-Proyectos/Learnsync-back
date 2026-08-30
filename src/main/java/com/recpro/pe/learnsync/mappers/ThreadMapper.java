package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import com.recpro.pe.learnsync.models.Thread;
import org.springframework.stereotype.Component;

@Component
public class ThreadMapper {

    public ThreadDTO toDto(Thread thread) {
        if (thread == null) return null;
        return new ThreadDTO(
                thread.getIdThread(),
                thread.getTitle(),
                thread.getMessage(),
                thread.getCreationDate(),
                thread.getLikes(),
                thread.getStars(),
                thread.getFile(),
                thread.getUser() != null ? thread.getUser().getUsername() : null,
                thread.getUser() != null ? thread.getUser().getProfilePhoto() : null,
                thread.getTopic() != null ? thread.getTopic().getName() : null,
                thread.getComments() != null ? thread.getComments().size() : 0
        );
    }
}
