package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.models.Topic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {

    @Autowired private ModelMapper modelMapper;

    public TopicDTO toDTO(Topic topic){
        return modelMapper.map(topic, TopicDTO.class);
    }
}
