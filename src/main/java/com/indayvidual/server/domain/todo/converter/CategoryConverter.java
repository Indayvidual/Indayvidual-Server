package com.indayvidual.server.domain.todo.converter;

import com.indayvidual.server.domain.todo.dto.request.CategoryCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.CategoryResponseDTO;
import com.indayvidual.server.domain.todo.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {

    public Category toEntity(CategoryCreateRequestDTO request, Long userId) {
        return Category.builder()
                .userId(userId)
                .title(request.getName())
                .color(request.getColor())
                .build();
    }

    public CategoryResponseDTO toResponse(Category category) {
        return CategoryResponseDTO.builder()
                .categoryId(category.getId())
                .name(category.getTitle())
                .color(category.getColor())
                .build();
    }

    public List<CategoryResponseDTO> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}