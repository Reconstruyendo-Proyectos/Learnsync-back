package com.recpro.pe.learnsync.mappers;

import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import com.recpro.pe.learnsync.models.Thread;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadMapper {

    @Autowired private ModelMapper modelMapper;

    public ThreadDTO toDTO(Thread thread){
        return modelMapper.map(thread, ThreadDTO.class);
    }
}
