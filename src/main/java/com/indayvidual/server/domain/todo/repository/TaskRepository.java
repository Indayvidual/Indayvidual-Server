package com.indayvidual.server.domain.todo.repository;

import com.indayvidual.server.domain.todo.entity.Task;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // taskId, userId로 task 조회
    Optional<Task> findByIdAndUserId(Long id, Long userId);

    // userId, CategoryId, DueDate 로 조회. task 조회용
    List<Task> findByUserIdAndCategoryIdAndDueDate(Long userId, Long categoryId, LocalDate date);

    /**
     * 특정 날짜와 카테고리 내에서 현재 등록된 Task 중 최대 position인 Task을 조회합니다.
     * <p>
     * - 새로운 Task 생성 시 position 지정에 사용됩니다.
     *
     * @param categoryId
     * @param dueDate
     * @return max position 값의 task
     */
    Optional<Task> findTopByCategoryIdAndDueDateOrderByPositionDesc(Long categoryId, LocalDate dueDate);

    /**
     * 지정한 사용자, 카테고리, 날짜에 속한 모든 task를 position 오름차순으로 조회합니다.
     * <p>
     * - 순서 및 카테고리 변경 시 사용합니다.
     *
     * @param userId
     * @param categoryId
     * @param dueDate
     * @return 오름차순으로 정렬된 task 목록
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Task t " +
            "where t.user.id = :userId and t.category.id = :categoryId and t.dueDate = :dueDate " +
            "order by t.position asc")
    List<Task> findAllByUserIdAndCategoryIdAndDueDateOrderByPosition(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("dueDate") LocalDate dueDate
    );

    List<Task> findAllByCategoryId(Long categoryId);

    List<Task> findAllByUserIdAndCategoryIdAndDueDate(Long userId, Long categoryId, LocalDate dueDate);
}
