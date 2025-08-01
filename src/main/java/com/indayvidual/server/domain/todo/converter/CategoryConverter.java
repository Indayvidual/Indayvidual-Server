package com.indayvidual.server.domain.todo.converter;

import com.indayvidual.server.domain.todo.dto.request.CategoryCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.CategoryResponseDTO;
import com.indayvidual.server.domain.todo.entity.Category;
import com.indayvidual.server.domain.todo.entity.Color;
import com.indayvidual.server.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {

    public Category toEntity(CategoryCreateRequestDTO request, User user, Color color) {
        return Category.builder()
                .user(user)
                .title(request.getName())
                .color(color)
                .build();
    }

    public CategoryResponseDTO toResponse(Category category) {
        return CategoryResponseDTO.builder()
                .categoryId(category.getId())
                .name(category.getTitle())
                .colorId(category.getColor().getId())
                .build();
    }

    public List<CategoryResponseDTO> toResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}