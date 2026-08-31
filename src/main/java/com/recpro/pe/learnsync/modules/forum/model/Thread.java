package com.recpro.pe.learnsync.modules.forum.model;

import com.recpro.pe.learnsync.modules.auth.model.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "threads")
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thread")
    @EqualsAndHashCode.Include
    private Integer idThread;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "likes", nullable = false)
    private Integer likes;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "file")
    private String file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_topic", nullable = false, referencedColumnName = "id_topic")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id_user")
    private User user;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @PrePersist
    void prePersist() {
        if (creationDate == null) creationDate = LocalDateTime.now();
    }

    public Thread(Integer idThread, String title, String message, Integer likes, Integer stars, String file, Topic topic, User user, List<Comment> comments) {
        this.idThread = idThread;
        this.title = title;
        this.message = message;
        this.creationDate = LocalDateTime.now();
        this.likes = likes;
        this.stars = stars;
        this.file = file;
        this.topic = topic;
        this.user = user;
        this.comments = comments;
    }
}
