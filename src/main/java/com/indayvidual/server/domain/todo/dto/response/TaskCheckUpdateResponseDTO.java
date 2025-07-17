package com.indayvidual.server.domain.todo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskCheckUpdateResponseDTO {

    private Long taskId;
    private Boolean isCompleted;
}
