package com.recpro.pe.learnsync.models;

import com.recpro.pe.learnsync.dtos.forum.category.CategoryDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    @EqualsAndHashCode.Include
    private Integer idCategory;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Topic> topics;

    public Category(Integer idCategory, String name, List<Topic> topics) {
        this.idCategory = idCategory;
        this.name = name;
        this.topics = topics;
    }

    public static CategoryDTO toDTO(Category category) {
        List<TopicDTO> topics = new ArrayList<>();
        for (Topic topic : category.getTopics()){
            TopicDTO topicDTO = Topic.toDTO(topic);
            topics.add(topicDTO);
        }
        return new CategoryDTO(category.getIdCategory(), category.getName(), topics);
    }
}