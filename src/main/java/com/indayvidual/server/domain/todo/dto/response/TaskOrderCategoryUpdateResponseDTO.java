package com.indayvidual.server.domain.todo.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TaskOrderCategoryUpdateResponseDTO {

    private Integer updatedCount;
    private List<Long> affectedCategories;

    public TaskOrderCategoryUpdateResponseDTO(int updatedCount, List<Long> affectedCategories) {
        this.updatedCount = updatedCount;
        this.affectedCategories = affectedCategories;
    }
}
