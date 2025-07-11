package com.indayvidual.server.domain.todo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskCompleteUpdateRequestDTO {

    private Boolean isCompleted;
}