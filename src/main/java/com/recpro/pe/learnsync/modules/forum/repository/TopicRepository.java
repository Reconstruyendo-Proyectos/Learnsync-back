package com.recpro.pe.learnsync.modules.forum.repository;

import com.recpro.pe.learnsync.modules.forum.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    boolean existsTopicByName(String name);
    Optional<Topic> findBySlug(String slug);
}
