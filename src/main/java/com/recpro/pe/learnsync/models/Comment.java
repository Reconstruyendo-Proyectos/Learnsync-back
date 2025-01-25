package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.forum.comment.CommentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Integer idComment;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "creation_date", nullable = false)
    private final LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_thread", nullable = false, referencedColumnName = "id_thread")
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id_user")
    private User user;

    public static CommentDTO toDto(Comment comment) {
        return new CommentDTO(comment.getIdComment(), comment.getMessage(), comment.getCreationDate(), User.toDto(comment.getUser()));
    }
}
