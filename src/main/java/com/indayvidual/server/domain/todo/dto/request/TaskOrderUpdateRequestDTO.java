package com.indayvidual.server.domain.todo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class TaskOrderUpdateRequestDTO {

    @NotEmpty(message = "taskOrder 리스트는 비어 있을 수 없습니다.")
    private List<Long> taskOrder;
}
