package com.veterinary.auth.repository;

import com.veterinary.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    @Query("SELECT u FROM User u WHERE u.resetToken = :token AND u.resetTokenExpiry > :now")
    Optional<User> findByValidResetToken(@Param("token") String token, @Param("now") LocalDateTime now);
}