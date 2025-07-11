package com.indayvidual.server.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TaskUpdateResponseDTO {

    private Long taskId;
    private String title;
    private boolean isCompleted;
}