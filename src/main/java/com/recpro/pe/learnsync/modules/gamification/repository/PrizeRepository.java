package com.recpro.pe.learnsync.modules.gamification.repository;

import com.recpro.pe.learnsync.modules.gamification.model.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Integer> {
}
