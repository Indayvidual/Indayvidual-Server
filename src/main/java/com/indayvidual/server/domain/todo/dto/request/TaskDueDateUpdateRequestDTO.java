package com.indayvidual.server.domain.todo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDueDateUpdateRequestDTO {

    @NotNull(message = "date 는 필수입니다.")
    private LocalDate date;
}
