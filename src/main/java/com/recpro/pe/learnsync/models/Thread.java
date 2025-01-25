package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.auth.user.UserDTO;
import com.recpro.pe.learnsync.dtos.forum.comment.CommentDTO;
import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "threads")
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thread")
    private Integer idThread;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "creation_date", nullable = false)
    private final LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_topic", nullable = false, referencedColumnName = "id_topic")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id_user")
    private User user;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public static ThreadDTO toDTO(Thread thread){
        List<CommentDTO> comments = new ArrayList<>();
        for (Comment comment : thread.getComments()) {
            CommentDTO commentDTO = Comment.toDto(comment);
            comments.add(commentDTO);
        }
        return new ThreadDTO(thread.getIdThread(), thread.getTitle(), thread.getMessage(), thread.getCreationDate(), User.toDto(thread.getUser()), comments);
    }
}
