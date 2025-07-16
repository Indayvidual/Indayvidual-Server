package com.indayvidual.server.domain.todo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskCheckUpdateRequestDTO {

    @NotNull(message = "isCompleted 는 필수입니다.")
    private Boolean isCompleted;
}