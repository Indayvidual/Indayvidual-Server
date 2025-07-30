package com.indayvidual.server.domain.todo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ColorResponseDTO {

    private Long colorId;
    private String code;
}
