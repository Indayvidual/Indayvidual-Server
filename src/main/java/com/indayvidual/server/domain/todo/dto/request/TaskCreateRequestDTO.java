package com.indayvidual.server.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TaskCreateRequestDTO {

    @NotBlank(message = "title 은 비어 있을 수 없습니다.")
    private String title;

    @NotNull(message = "date 는 필수입니다.")
    private LocalDate date;
}