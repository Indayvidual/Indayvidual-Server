package com.indayvidual.server.domain.todo.service.category;

import com.indayvidual.server.domain.todo.converter.CategoryConverter;
import com.indayvidual.server.domain.todo.dto.request.CategoryCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.CategoryResponseDTO;
import com.indayvidual.server.domain.todo.entity.Category;
import com.indayvidual.server.domain.todo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    @Transactional
    public CategoryResponseDTO create(CategoryCreateRequestDTO request, Long userId) {
        Category entity = categoryConverter.toEntity(request, userId);
        Category saved = categoryRepository.save(entity);
        return categoryConverter.toResponse(saved);
    }
}