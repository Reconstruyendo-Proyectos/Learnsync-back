package com.recpro.pe.learnsync.modules.forum.repository;

import com.recpro.pe.learnsync.modules.forum.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
