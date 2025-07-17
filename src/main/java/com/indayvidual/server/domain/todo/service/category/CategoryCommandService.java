package com.indayvidual.server.domain.todo.service.category;

import com.indayvidual.server.domain.todo.converter.CategoryConverter;
import com.indayvidual.server.domain.todo.dto.request.CategoryCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.CategoryResponseDTO;
import com.indayvidual.server.domain.todo.entity.Category;
import com.indayvidual.server.domain.todo.repository.CategoryRepository;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final UserRepository userRepository;

    @Transactional
    public CategoryResponseDTO create(CategoryCreateRequestDTO request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Category entity = categoryConverter.toEntity(request, user);
        Category saved = categoryRepository.save(entity);
        return categoryConverter.toResponse(saved);
    }
}