package com.recpro.pe.learnsync.repos.prizes;

import com.recpro.pe.learnsync.models.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {
}
