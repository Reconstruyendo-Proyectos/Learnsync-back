package com.recpro.pe.learnsync.modules.gamification.repository;

import com.recpro.pe.learnsync.modules.gamification.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {
}
