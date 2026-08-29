package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
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
@Entity(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_topic")
    @EqualsAndHashCode.Include
    private Integer idTopic;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "topic_icon", nullable = false)
    private String topicIcon;

    @Column(name = "topic_poster", nullable = false)
    private String topicPoster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false, referencedColumnName = "id_category")
    private Category category;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Thread> threads;

    @PrePersist
    void prePersist() {
        if (creationDate == null) creationDate = LocalDateTime.now();
    }

    public Topic(Integer idTopic, String name, String description, String slug, String topicIcon, String topicPoster, Category category, List<Thread> threads) {
        this.idTopic = idTopic;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.creationDate = LocalDateTime.now();
        this.topicIcon = topicIcon;
        this.topicPoster = topicPoster;
        this.category = category;
        this.threads = threads;
    }

    public Topic(Integer idTopic, String name, String description, String slug, LocalDateTime creationDate, String topicIcon, String topicPoster, Category category, List<Thread> threads) {
        this.idTopic = idTopic;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.creationDate = creationDate != null ? creationDate : LocalDateTime.now();
        this.topicIcon = topicIcon;
        this.topicPoster = topicPoster;
        this.category = category;
        this.threads = threads;
    }

    public static TopicDTO toDTO(Topic topic){
        return new TopicDTO(topic.getIdTopic(), topic.getName(), topic.getDescription(), topic.getSlug(), topic.getTopicIcon(), topic.getTopicPoster(), topic.getThreads().size());
    }

    public static String transformName(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split("\\s+");
        StringBuilder capitalizedText = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                capitalizedText.append(capitalizedWord).append(" ");
            }
        }

        return capitalizedText.toString().trim();
    }
}
