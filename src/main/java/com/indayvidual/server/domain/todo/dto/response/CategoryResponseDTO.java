package com.indayvidual.server.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryResponseDTO {

    private Long categoryId;
    private String name;
    private String color;
}
