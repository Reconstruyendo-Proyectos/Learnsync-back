package com.recpro.pe.learnsync.dtos.forum.topic;

import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TopicDTO {
    private Integer idTopic;
    private String name;
    private String description;
    private String slug;
    private List<ThreadDTO> threads;
}
