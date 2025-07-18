package com.indayvidual.server.domain.todo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskUpdateResponseDTO {

    private Long taskId;
    private String title;
    private boolean isCompleted;
}