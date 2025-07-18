package com.indayvidual.server.domain.todo.repository;

import com.indayvidual.server.domain.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // taskId, userId로 task 조회
    Optional<Task> findByIdAndUserId(Long id, Long userId);

    // userId, CategoryId, DueDate 로 조회. task 조회용
    List<Task> findByUserIdAndCategoryIdAndDueDate(Long userId, Long categoryId, LocalDate date);

    /**
     * 카테고리 내에서 현재 등록된 Task의 최대 position을 조회합니다.
     * <p>
     * - 새로운 Task 생성 시 position 지정에 사용됩니다.
     *
     * @param categoryId
     * @return max position 값 (null 가능)
     */
    @Query("SELECT MAX(t.position) FROM Task t WHERE t.category.id = :categoryId")
    Integer findMaxPositionByCategoryId(@Param("categoryId") Long categoryId);
}
