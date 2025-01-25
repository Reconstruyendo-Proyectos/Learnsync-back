package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
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
@Entity(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_topic")
    private Integer idTopic;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "creation_date", nullable = false)
    private final LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false, referencedColumnName = "id_category")
    private Category category;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Thread> threads;

    public static TopicDTO toDTO(Topic topic){
        List<ThreadDTO> threads = new ArrayList<>();
        for (Thread thread : topic.getThreads()) {
            ThreadDTO threadDTO = Thread.toDTO(thread);
            threads.add(threadDTO);
        }
        return new TopicDTO(topic.getIdTopic(), topic.getName(), topic.getDescription(), topic.getSlug(), threads);
    }
}
