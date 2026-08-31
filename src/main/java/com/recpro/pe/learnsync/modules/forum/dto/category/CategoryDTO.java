package com.recpro.pe.learnsync.modules.forum.dto.category;

import com.recpro.pe.learnsync.modules.forum.dto.topic.TopicDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@AllArgsConstructor
@Data
public class CategoryDTO {
    private Integer idCategory;
    private String name;
    private List<TopicDTO> topics;
}
