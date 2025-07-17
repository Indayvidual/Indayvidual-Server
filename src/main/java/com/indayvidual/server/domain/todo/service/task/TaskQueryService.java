package com.indayvidual.server.domain.todo.service.task;

import com.indayvidual.server.domain.todo.dto.response.TaskResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface TaskQueryService {

    /**
     * 특정 사용자, 카테고리, 날짜에 해당하는 할 일 목록을 조회합니다.
     *
     * @param userId     사용자 ID
     * @param categoryId 카테고리 ID
     * @param date       조회할 날짜 (yyyy-MM-dd)
     * @return 해당 조건에 맞는 할 일 목록
     */
    List<TaskResponseDTO> findTasksByCategoryAndDate(Long userId, Long categoryId, LocalDate date);
}
