package com.indayvidual.server.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreateRequestDTO {

    @NotBlank(message = "title 은 비어 있을 수 없습니다.")
    private String name;

    @NotNull(message = "color 은 비어 있을 수 없습니다.")
    private Long colorId;
}