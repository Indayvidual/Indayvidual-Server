package com.indayvidual.server.domain.user.repository;

import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, Status status);
}