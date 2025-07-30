package com.indayvidual.server.domain.todo.repository;

import com.indayvidual.server.domain.todo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Category> findByIdAndUserId(Long userId, Long categoryId);
}
