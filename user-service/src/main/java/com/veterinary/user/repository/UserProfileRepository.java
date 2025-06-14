package com.veterinary.user.repository;

import com.veterinary.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUsername(String username);

    Optional<UserProfile> findByEmail(String email);

    Optional<UserProfile> findByAuthUserId(Long authUserId);

    List<UserProfile> findByRole(UserProfile.Role role);

    List<UserProfile> findByIsActive(Boolean isActive);

    List<UserProfile> findByRoleAndIsActive(UserProfile.Role role, Boolean isActive);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByAuthUserId(Long authUserId);

    @Query("SELECT u FROM UserProfile u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<UserProfile> findByFirstNameContainingOrLastNameContaining(@Param("name") String name);

    @Query("SELECT u FROM UserProfile u WHERE u.role = :role AND u.isActive = true ORDER BY u.firstName, u.lastName")
    List<UserProfile> findActiveUsersByRole(@Param("role") UserProfile.Role role);

    @Query("SELECT COUNT(u) FROM UserProfile u WHERE u.role = :role AND u.isActive = true")
    Long countActiveUsersByRole(@Param("role") UserProfile.Role role);
}