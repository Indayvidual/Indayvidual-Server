package com.indayvidual.server.domain.todo.converter;

import com.indayvidual.server.domain.todo.dto.request.TaskCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskCheckUpdateResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskUpdateResponseDTO;
import com.indayvidual.server.domain.todo.entity.Category;
import com.indayvidual.server.domain.todo.entity.Task;
import com.indayvidual.server.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskConverter {

    public Task toEntity(TaskCreateRequestDTO dto, Category category, User user, Integer position) {
        return Task.builder()
                .user(user)
                .category(category)
                .title(dto.getTitle())
                .dueDate(dto.getDate())
                .position(position)
                .isChecked(false) // 생성 시 기본값 고정
                .build();
    }

    public TaskResponseDTO toResponse(Task task) {
        return TaskResponseDTO.builder()
                .taskId(task.getId())
                .categoryId(task.getCategory().getId())
                .title(task.getTitle())
                .isCompleted(task.isChecked())
                .order(task.getPosition())
                .date(task.getDueDate().toString())
                .build();
    }

    public List<TaskResponseDTO> toResponseList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskUpdateResponseDTO toUpdateResponse(Task task) {
        return TaskUpdateResponseDTO.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .isCompleted(task.isChecked())
                .build();
    }

    public TaskCheckUpdateResponseDTO toCheckUpdateResponse(Task task) {
        return TaskCheckUpdateResponseDTO.builder()
                .taskId(task.getId())
                .isCompleted(task.isChecked())
                .build();
    }
}

