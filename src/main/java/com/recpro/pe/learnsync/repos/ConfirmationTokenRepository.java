package com.recpro.pe.learnsync.repos;

import com.recpro.pe.learnsync.models.ConfirmationToken;
import com.recpro.pe.learnsync.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {

    boolean existsConfirmationTokenByToken(String token);

    ConfirmationToken findByToken(String token);

    ConfirmationToken saveAndFlush(ConfirmationToken confirmationToken);
    ConfirmationToken findByUser(User user);
}
