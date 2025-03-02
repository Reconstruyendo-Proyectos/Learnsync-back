package com.recpro.pe.learnsync.repos.forum;

import com.recpro.pe.learnsync.models.Thread;
import com.recpro.pe.learnsync.models.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Integer> {
    Page<Thread> findAll(Pageable pageable);
    Page<Thread> findByOrderByCreationDateDesc(Pageable pageable);
    Page<Thread> findByOrderByLikesDesc(Pageable pageable);
    Integer countByTopic(Topic topic);
}
