package com.recpro.pe.learnsync.modules.forum.model;

import com.recpro.pe.learnsync.modules.auth.model.User;
import com.recpro.pe.learnsync.modules.forum.dto.comment.CommentDTO;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    @EqualsAndHashCode.Include
    private Integer idComment;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_thread", nullable = false, referencedColumnName = "id_thread")
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id_user")
    private User user;

    @PrePersist
    void prePersist() {
        if (creationDate == null) creationDate = LocalDateTime.now();
    }

    public Comment(Integer idComment, String message, Thread thread, User user) {
        this.idComment = idComment;
        this.message = message;
        this.creationDate = LocalDateTime.now();
        this.thread = thread;
        this.user = user;
    }

    public static CommentDTO toDto(Comment comment) {
        return new CommentDTO(comment.getIdComment(), comment.getMessage(), comment.getCreationDate(), comment.getUser().getUsername(), comment.getUser().getProfilePhoto());
    }
}
