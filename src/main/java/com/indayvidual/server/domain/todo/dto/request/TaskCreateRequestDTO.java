package com.indayvidual.server.domain.todo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskCreateRequestDTO {

    private String title;
    private String date;
}