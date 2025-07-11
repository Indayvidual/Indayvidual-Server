package com.indayvidual.server.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TaskResponseDTO {

    private Long taskId;
    private Long categoryId;
    private String title;
    private boolean isCompleted;
    private int order;
    private String date;
}
