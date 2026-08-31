package com.recpro.pe.learnsync.modules.forum.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicDTO {
    private Integer idTopic;
    private String name;
    private String description;
    private String slug;
    private String topicIcon;
    private String topicPoster;
    private Integer nroThreads;
}
