package com.recpro.pe.learnsync.dtos.category;

import com.recpro.pe.learnsync.dtos.topic.TopicDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CategoryDTO {
    private String name;
    private String description;
    private List<TopicDTO> topics;
}
