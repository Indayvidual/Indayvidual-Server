package com.indayvidual.server.domain.todo.service.category;

import com.indayvidual.server.domain.todo.converter.CategoryConverter;
import com.indayvidual.server.domain.todo.dto.request.CategoryCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.CategoryResponseDTO;
import com.indayvidual.server.domain.todo.entity.Category;
import com.indayvidual.server.domain.todo.entity.Task;
import com.indayvidual.server.domain.todo.repository.CategoryRepository;
import com.indayvidual.server.domain.todo.repository.TaskRepository;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
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

    @Transactional
    public void delete(Long userId, Long categoryId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TASK_CATEGORY_NOT_FOUND));

        // 연관된 task 먼저 삭제
        List<Task> tasks = taskRepository.findAllByCategoryId(categoryId);
        taskRepository.deleteAll(tasks);

        categoryRepository.delete(category);

        log.debug("[CATEGORY] 삭제 완료 - categoryId={}", categoryId);
    }
}