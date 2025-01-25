package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.forum.category.CategoryDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Integer idCategory;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    // Mapear 1 a muchos con Topic
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Topic> topics;

    public static CategoryDTO toDTO(Category category) {
        List<TopicDTO> topics = new ArrayList<>();
        for (Topic topic : category.getTopics()){
            TopicDTO topicDTO = Topic.toDTO(topic);
            topics.add(topicDTO);
        }
        return new CategoryDTO(category.getIdCategory(), category.getName(), category.getDescription(), topics);
    }
}