package com.indayvidual.server.domain.todo.service.task;

import com.indayvidual.server.domain.todo.dto.request.TaskCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.request.TaskDueDateUpdateRequestDTO;
import com.indayvidual.server.domain.todo.dto.request.TaskOrderUpdateRequestDTO;
import com.indayvidual.server.domain.todo.dto.request.TaskTitleUpdateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskCheckUpdateResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskUpdateResponseDTO;

public interface TaskCommandService {

    /**
     * 할 일을 생성합니다.
     *
     * @param userId
     * @param categoryId
     * @param request    요청 DTO
     * @return 할 일 응답 DTO
     */
    TaskResponseDTO createTask(Long userId, Long categoryId, TaskCreateRequestDTO request);

    /**
     * 기존 할 일의 제목을 수정합니다.
     *
     * @param userId
     * @param taskId
     * @param request 요청 DTO
     * @return 수정된 할 일 응답 DTO
     */
    TaskUpdateResponseDTO updateTaskTitle(Long userId, Long taskId, TaskTitleUpdateRequestDTO request);

    /**
     * 기존 할 일의 날짜를 수정합니다.
     *
     * @param userId
     * @param taskId
     * @param request 요청 DTO
     * @return 수정된 할 일 응답 DTO
     */
    TaskResponseDTO updateTaskDueDate(Long userId, Long taskId, TaskDueDateUpdateRequestDTO request);

    /**
     * 특정 카테고리 내 할 일의 순서를 일괄 변경합니다.
     *
     * @param userId
     * @param categoryId
     * @param request    요청 DTO
     */
    void updateTaskOrder(Long userId, Long categoryId, TaskOrderUpdateRequestDTO request);

    /**
     * 할 일의 체크 상태를 토글합니다.
     *
     * @param userId
     * @param taskId
     * @return 상태가 변경된 응답 DTO
     */
    TaskCheckUpdateResponseDTO toggleCheck(Long userId, Long taskId);

    /**
     * 할 일을 삭제합니다.
     *
     * @param userId
     * @param taskId
     */
    void deleteTask(Long userId, Long taskId);
}
