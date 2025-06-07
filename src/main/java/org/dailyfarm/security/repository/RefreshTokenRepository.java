package org.dailyfarm.security.repository;

import java.util.Optional;

import org.dailyfarm.security.entity.RefreshToken;
import org.dailyfarm.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUser(User user);
}
