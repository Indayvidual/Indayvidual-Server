package com.indayvidual.server.domain.todo.controller;

import com.indayvidual.server.domain.todo.dto.request.*;
import com.indayvidual.server.domain.todo.dto.response.TaskCheckUpdateResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskUpdateResponseDTO;
import com.indayvidual.server.domain.todo.service.task.TaskCommandService;
import com.indayvidual.server.domain.todo.service.task.TaskQueryService;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Todo 할 일 API", description = "Todo 할 일 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/todo")
public class TodoTaskController {

    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;

    @Operation(summary = "할 일 목록 조회", description = "특정 날짜, 카테고리 ID에 해당하는 할 일 목록을 조회합니다.")
    @GetMapping("/categories/{categoryId}/tasks")
    public ApiResponse<List<TaskResponseDTO>> getTasks(
            @PathVariable Long categoryId,
            @Parameter(
                    name = "date",
                    description = "조회할 날짜 (형식: yyyy-MM-dd)",
                    example = "2025-07-17"
            )
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(
                taskQueryService.findTasksByCategoryAndDate(userId, categoryId, date),
                SuccessStatus.GET_TASKS_SUCCESS.getCode(),
                SuccessStatus.GET_TASKS_SUCCESS.getMessage());
    }

    @Operation(summary = "할 일 등록", description = "새로운 할 일을 등록합니다.")
    @PostMapping("/categories/{categoryId}/tasks")
    public ApiResponse<TaskResponseDTO> createTask(
            @PathVariable Long categoryId,
            @RequestBody @Valid TaskCreateRequestDTO request) {

        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(
                taskCommandService.createTask(userId, categoryId, request),
                SuccessStatus.CREATE_TASK_SUCCESS.getCode(),
                SuccessStatus.CREATE_TASK_SUCCESS.getMessage());
    }

    @Operation(summary = "할 일 제목 수정", description = "할 일의 제목을 수정합니다.")
    @PatchMapping("/tasks/{taskId}/title")
    public ApiResponse<TaskUpdateResponseDTO> updateTaskTitle(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskTitleUpdateRequestDTO request) {

        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(
                taskCommandService.updateTaskTitle(userId, taskId, request),
                SuccessStatus.UPDATE_TASK_TITLE_SUCCESS.getCode(),
                SuccessStatus.UPDATE_TASK_TITLE_SUCCESS.getMessage());
    }

    @Operation(summary = "할 일 날짜 수정", description = "할 일의 날짜를 수정합니다.")
    @PatchMapping("/tasks/{taskId}/due-date")
    public ApiResponse<TaskResponseDTO> updateTaskDueDate(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskDueDateUpdateRequestDTO request) {

        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(
                taskCommandService.updateTaskDueDate(userId, taskId, request),
                SuccessStatus.UPDATE_TASK_DUE_DATE_SUCCESS.getCode(),
                SuccessStatus.UPDATE_TASK_DUE_DATE_SUCCESS.getMessage());
    }

    @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.")
    @DeleteMapping("/tasks/{taskId}")
    public ApiResponse<Void> deleteTask(@PathVariable Long taskId) {
        Long userId = Utils.getUserId();
        taskCommandService.deleteTask(userId, taskId);
        return ApiResponse.onSuccess(
                null,
                SuccessStatus.DELETE_TASK_SUCCESS.getCode(),
                SuccessStatus.DELETE_TASK_SUCCESS.getMessage());
    }

    @Operation(summary = "할 일 체크/체크 해제", description = "할 일을 체크하거나 체크 해제합니다.")
    @PatchMapping("/tasks/{taskId}/check")
    public ApiResponse<TaskCheckUpdateResponseDTO> updateTaskStatus(@PathVariable Long taskId) {
        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(
                taskCommandService.toggleCheck(userId, taskId),
                SuccessStatus.UPDATE_TASK_CHECK_SUCCESS.getCode(),
                SuccessStatus.UPDATE_TASK_CHECK_SUCCESS.getMessage());
    }

    @Operation(summary = "할 일 순서 변경",
            description = """
                    카테고리 내 할 일의 순서를 변경합니다.\n
                    **해당 카테고리 내 모든 Task ID**를 정렬 순서대로 request body로 전달해야 합니다.\n  
                    해당 순서를 기준으로 `position` 필드를 재정렬합니다.
                    """)
    @PatchMapping("/categories/{categoryId}/tasks/order")
    public ApiResponse<Void> updateTaskOrder(
            @PathVariable Long categoryId,
            @RequestBody @Valid TaskOrderUpdateRequestDTO request) {

        //TODO : 카테고리 변경도 추가하기
        Long userId = Utils.getUserId();
        taskCommandService.updateTaskOrder(userId, categoryId, request);
        return ApiResponse.onSuccess(
                null,
                SuccessStatus.UPDATE_TASK_ORDER_SUCCESS.getCode(),
                SuccessStatus.UPDATE_TASK_ORDER_SUCCESS.getMessage());
    }
}

