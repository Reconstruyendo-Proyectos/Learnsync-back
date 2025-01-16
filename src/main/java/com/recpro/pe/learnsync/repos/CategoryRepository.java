package com.recpro.pe.learnsync.repos;

import com.recpro.pe.learnsync.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAll(Pageable pageable);
    Category findByName(String name);
    boolean existsCategoryByName(String name);
}
