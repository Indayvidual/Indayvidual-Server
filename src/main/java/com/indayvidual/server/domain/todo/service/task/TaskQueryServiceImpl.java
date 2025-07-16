package com.indayvidual.server.domain.todo.service.task;

import com.indayvidual.server.domain.todo.converter.TaskConverter;
import com.indayvidual.server.domain.todo.dto.response.TaskResponseDTO;
import com.indayvidual.server.domain.todo.entity.Task;
import com.indayvidual.server.domain.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskQueryServiceImpl implements TaskQueryService {

    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findTasksByCategoryAndDate(Long userId, Long categoryId, LocalDate date) {
        log.debug("[TASK-QUERY] 할 일 조회 요청 - userId={}, categoryId={}, date={}", userId, categoryId, date);

        List<Task> tasks = taskRepository.findByUserIdAndCategoryIdAndDate(userId, categoryId, date);

        log.debug("[TASK-QUERY] 조회된 할 일 수: {}", tasks.size());

        return taskConverter.toResponseList(tasks);
    }
}