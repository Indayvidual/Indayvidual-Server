package com.indayvidual.server.domain.todo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskResponseDTO {

    private Long taskId;
    private Long categoryId;
    private String title;
    private Boolean isCompleted;
    private Integer order;
    private String date;
}
