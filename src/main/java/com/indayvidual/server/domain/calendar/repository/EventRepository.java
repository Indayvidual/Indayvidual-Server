package com.indayvidual.server.domain.calendar.repository;

import com.indayvidual.server.domain.calendar.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}
