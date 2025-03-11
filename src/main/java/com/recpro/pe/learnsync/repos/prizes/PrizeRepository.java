package com.recpro.pe.learnsync.repos.prizes;

import com.recpro.pe.learnsync.models.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Integer> {
}
