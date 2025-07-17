package com.indayvidual.server.domain.todo.service.task;

import com.indayvidual.server.domain.todo.converter.TaskConverter;
import com.indayvidual.server.domain.todo.dto.request.TaskCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.request.TaskDueDateUpdateRequestDTO;
import com.indayvidual.server.domain.todo.dto.request.TaskOrderUpdateRequestDTO;
import com.indayvidual.server.domain.todo.dto.request.TaskTitleUpdateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskCheckUpdateResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskUpdateResponseDTO;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskCommandServiceImpl implements TaskCommandService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TaskConverter taskConverter;
    private final UserRepository userRepository;

    /**
     * 할 일을 등록합니다.
     */
    @Transactional
    public TaskResponseDTO createTask(Long userId, Long categoryId, TaskCreateRequestDTO request) {
        log.debug("[TASK] 사용자 {}의 새로운 할 일 등록 요청 - {}", userId, request.getTitle());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND));

        // position 지정
        Integer position = generateNextPosition(categoryId);

        Task task = taskConverter.toEntity(request, category, user, position);
        taskRepository.save(task);

        log.debug("[TASK] 등록 완료 - taskId={}, title={}", task.getId(), task.getTitle());
        return taskConverter.toResponse(task);
    }

    /**
     * 새로운 할 일의 position 을 계산합니다.
     * 카테고리 내 task의 최대 position을 조회한 후,
     * 없으면 0번, 있으면 max+1로 지정합니다.
     *
     * @param categoryId
     * @return 새로운 할 일의 position
     */
    private Integer generateNextPosition(Long categoryId) {
        Integer max = taskRepository.findMaxPositionByCategoryId(categoryId);
        if (max == null) {
            log.debug("[TASK][generateNextPosition] max가 null이므로 기본값 0으로 시작합니다.");
            return 0;
        }
        return max + 1;
    }

    /**
     * 할 일 제목을 수정합니다.
     */
    @Transactional
    public TaskUpdateResponseDTO updateTaskTitle(Long userId, Long taskId, TaskTitleUpdateRequestDTO request) {
        Task task = findTaskByIdAndUserId(taskId, userId);

        task.updateTitle(request.getTitle());
        log.debug("[TASK] 제목 수정 완료 - taskId={}, title={}", task.getId(), task.getTitle());
        return taskConverter.toUpdateResponse(task);
    }

    /**
     * 할 일 날짜를 수정합니다.
     */
    @Transactional
    public TaskResponseDTO updateTaskDueDate(Long userId, Long taskId, TaskDueDateUpdateRequestDTO request) {
        Task task = findTaskByIdAndUserId(taskId, userId);

        task.updateDueDate(request.getDueDate());

        log.debug("[TASK] 날짜 수정 완료 - taskId={}, title={}", task.getId(), task.getTitle());
        return taskConverter.toResponse(task);
    }

    /**
     * 할 일을 삭제합니다.
     */
    @Transactional
    public void deleteTask(Long userId, Long taskId) {
        Task task = findTaskByIdAndUserId(taskId, userId);
        taskRepository.delete(task);

        log.debug("[TASK] 삭제 완료 - taskId={}", taskId);
    }

    /**
     * 할 일 체크를 토글합니다.
     */
    @Transactional
    public TaskCheckUpdateResponseDTO toggleCheck(Long userId, Long taskId) {
        Task task = findTaskByIdAndUserId(taskId, userId);
        task.toggleChecked();

        log.debug("[TASK] 체크 상태 변경 - taskId={}, isChecked={}", taskId, task.isChecked());
        return taskConverter.toCheckUpdateResponse(task);
    }

    /**
     * 카테고리 내 할 일 순서를 변경합니다.
     */
    @Transactional
    public void updateTaskOrder(Long userId, Long categoryId, TaskOrderUpdateRequestDTO request) {
        List<Long> taskIds = validateTaskOrderRequest(request);
        List<Task> tasks = findAndValidateTasks(userId, categoryId, taskIds);
        applyTaskPositionChanges(tasks, taskIds);

        log.debug("[TASK] 순서 변경 완료 - categoryId={}, userId={}, taskCount={}",
                categoryId, userId, taskIds.size());
    }

    /**
     * Task 순서 변경 요청의 유효성을 검증합니다.
     */
    private List<Long> validateTaskOrderRequest(TaskOrderUpdateRequestDTO request) {
        List<Long> taskIds = request.getTaskOrder();
        if (taskIds == null || taskIds.isEmpty()) {
            log.warn("[TASK] 빈 taskOrder 요청");
            throw new GeneralException(ErrorStatus.INVALID_TASK_ORDER);
        }
        return taskIds;
    }

    /**
     * Task를 조회, 검증합니다.
     *
     * @param userId
     * @param categoryId
     * @param taskIds
     * @return Task list
     */
    private List<Task> findAndValidateTasks(Long userId, Long categoryId, List<Long> taskIds) {
        List<Task> tasks = taskRepository.findAllById(taskIds);
        if (tasks.size() != taskIds.size()) {
            log.warn("[TASK] 일부 taskId 존재하지 않음");
            throw new GeneralException(ErrorStatus.TASK_NOT_FOUND);
        }

        for (Task task : tasks) {
            if (!task.getCategory().getId().equals(categoryId) || !task.getUser().getId().equals(userId)) {
                log.warn("[TASK] 잘못된 소속의 task 존재 - taskId={}", task.getId());
                throw new GeneralException(ErrorStatus.TASK_FORBIDDEN);
            }
        }

        return tasks;
    }

    /**
     * Task에 순서를 적용합니다.
     *
     * @param tasks
     * @param orderedIds
     */
    private void applyTaskPositionChanges(List<Task> tasks, List<Long> orderedIds) {
        Map<Long, Task> taskMap = tasks.stream()
                .collect(Collectors.toMap(Task::getId, t -> t));

        for (int i = 0; i < orderedIds.size(); i++) {
            Long taskId = orderedIds.get(i);
            Task task = taskMap.get(taskId);
            task.updatePosition(i);
        }
    }

    private Task findTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TASK_NOT_FOUND));
    }
}
