package com.indayvidual.server.domain.todo.controller;

import com.indayvidual.server.domain.todo.dto.request.*;
import com.indayvidual.server.domain.todo.dto.response.TaskCheckUpdateResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskUpdateResponseDTO;
import com.indayvidual.server.domain.todo.service.task.TaskCommandService;
import com.indayvidual.server.domain.todo.service.task.TaskQueryService;
import com.indayvidual.server.global.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Todo API", description = "Todo 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/todo")
public class TodoTaskController {
    // todo: success status 추가

    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;

    @Operation(summary = "할 일 목록 조회", description = "특정 날짜, 카테고리 ID에 해당하는 할 일 목록을 조회합니다.")
    @GetMapping("/categories/{categoryId}/tasks")
    public ApiResponse<List<TaskResponseDTO>> getTasks(
            @PathVariable Long categoryId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return ApiResponse.onSuccess(taskQueryService.findTasksByCategoryAndDate(userId, categoryId, date));
    }

    @Operation(summary = "할 일 등록", description = "새로운 할 일을 등록합니다.")
    @PostMapping("/categories/{categoryId}/tasks")
    public ApiResponse<TaskResponseDTO> createTask(
            @PathVariable Long categoryId,
            @RequestBody @Valid TaskCreateRequestDTO request) {

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return ApiResponse.onSuccess(taskCommandService.createTask(userId, categoryId, request));
    }

    @Operation(summary = "할 일 제목 수정", description = "할 일의 제목을 수정합니다.")
    @PatchMapping("/tasks/{taskId}/title")
    public ApiResponse<TaskUpdateResponseDTO> updateTaskTitle(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskTitleUpdateRequestDTO request) {

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return ApiResponse.onSuccess(taskCommandService.updateTaskTitle(userId, taskId, request));
    }

    @Operation(summary = "할 일 날짜 수정", description = "할 일의 날짜를 수정합니다.")
    @PatchMapping("/tasks/{taskId}/due-date")
    public ApiResponse<TaskResponseDTO> updateTaskDueDate(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskDueDateUpdateRequestDTO request) {

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return ApiResponse.onSuccess(taskCommandService.updateTaskDueDate(userId, taskId, request));
    }

    @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.")
    @DeleteMapping("/tasks/{taskId}")
    public ApiResponse<Void> deleteTask(@PathVariable Long taskId) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        taskCommandService.deleteTask(userId, taskId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "할 일 체크/체크 해제", description = "할 일을 체크하거나 체크 해제합니다.")
    @PatchMapping("/tasks/{taskId}/check")
    public ApiResponse<TaskCheckUpdateResponseDTO> updateTaskStatus(@PathVariable Long taskId) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return ApiResponse.onSuccess(taskCommandService.toggleCheck(userId, taskId));
    }

    @Operation(summary = "할 일 순서 변경", description = "카테고리 내 할 일의 순서를 변경합니다.")
    @PatchMapping("/categories/{categoryId}/tasks/order")
    public ApiResponse<Void> updateTaskOrder(
            @PathVariable Long categoryId,
            @RequestBody @Valid TaskOrderUpdateRequestDTO request) {

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        taskCommandService.updateTaskOrder(userId, categoryId, request);
        return ApiResponse.onSuccess(null);
    }
}

