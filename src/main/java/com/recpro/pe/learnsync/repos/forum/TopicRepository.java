package com.recpro.pe.learnsync.repos.forum;

import com.recpro.pe.learnsync.models.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Page<Topic> findAll(Pageable pageable);
    boolean existsTopicByName(String name);
    boolean existsTopicBySlug(String slug);
    Optional<Topic> findByName(String name);
}
