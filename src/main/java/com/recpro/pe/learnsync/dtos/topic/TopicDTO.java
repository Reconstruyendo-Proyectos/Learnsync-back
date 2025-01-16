package com.recpro.pe.learnsync.dtos.topic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicDTO {
    private Integer id;
    private String name;
    private String description;
}
