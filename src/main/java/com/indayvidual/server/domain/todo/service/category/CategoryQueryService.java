package com.indayvidual.server.domain.todo.service.category;

import com.indayvidual.server.domain.todo.converter.CategoryConverter;
import com.indayvidual.server.domain.todo.dto.response.CategoryResponseDTO;
import com.indayvidual.server.domain.todo.entity.Category;
import com.indayvidual.server.domain.todo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    public List<CategoryResponseDTO> findAll(Long userId) {
        List<Category> categories = categoryRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        return categoryConverter.toResponseList(categories);
    }
}