package com.indayvidual.server.domain.todo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskOrderCategoryUpdateRequestDTO {

    @NotNull(message = "taskId는 필수입니다.")
    private Long taskId;

    @NotNull(message = "categoryId는 필수입니다.")
    private Long categoryId;

    @NotNull(message = "order는 필수입니다.")
    @Min(value = 0, message = "order는 0 이상이어야 합니다.")
    private Integer order;
}