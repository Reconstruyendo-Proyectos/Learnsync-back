package com.recpro.pe.learnsync.dtos.forum.category;

import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CategoryDTO {
    private Integer idCategory;
    private String name;
    private String description;
    private List<TopicDTO> topics;
}
