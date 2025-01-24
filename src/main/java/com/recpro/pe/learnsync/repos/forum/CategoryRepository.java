package com.recpro.pe.learnsync.repos.forum;

import com.recpro.pe.learnsync.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAll(Pageable pageable);
    Optional<Category> findByName(String name);
    boolean existsCategoryByName(String name);
}
