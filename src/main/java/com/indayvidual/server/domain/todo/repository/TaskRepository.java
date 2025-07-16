package com.indayvidual.server.domain.todo.repository;

import com.indayvidual.server.domain.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    List<Task> findByUserIdAndCategoryIdAndDate(Long userId, Long categoryId, LocalDate date);
}
