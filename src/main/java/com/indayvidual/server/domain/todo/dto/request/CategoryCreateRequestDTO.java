package com.indayvidual.server.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreateRequestDTO {

    @NotBlank(message = "name 은 비어 있을 수 없습니다.")
    private String name;

    @NotBlank(message = "color 은 비어 있을 수 없습니다.")
    private String color;
}