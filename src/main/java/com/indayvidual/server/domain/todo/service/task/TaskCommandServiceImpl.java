package com.indayvidual.server.domain.todo.service.task;

import com.indayvidual.server.domain.todo.converter.TaskConverter;
import com.indayvidual.server.domain.todo.dto.request.*;
import com.indayvidual.server.domain.todo.dto.response.TaskCheckUpdateResponseDTO;
import com.indayvidual.server.domain.todo.dto.response.TaskOrderCategoryUpdateResponseDTO;
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

import java.time.LocalDate;
import java.util.*;
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
                .orElseThrow(() -> new GeneralException(ErrorStatus.TASK_CATEGORY_NOT_FOUND));

        // position 지정
        Integer position = generateNextPosition(categoryId, request.getDate());

        Task task = taskConverter.toEntity(request, category, user, position);
        taskRepository.save(task);

        log.debug("[TASK] 등록 완료 - taskId={}, title={}", task.getId(), task.getTitle());
        return taskConverter.toResponse(task);
    }

    /**
     * 새로운 할 일의 position 을 계산합니다.
     * 특정 날짜와 카테고리 내 task의 최대 position을 조회한 후,
     * 없으면 0, 있으면 max+1로 지정합니다.
     *
     * @param categoryId
     * @param date
     * @return 새로운 할 일의 position
     */
    private Integer generateNextPosition(Long categoryId, LocalDate date) {
        Integer max = taskRepository
                .findTopByCategoryIdAndDueDateOrderByPositionDesc(categoryId, date)
                .map(Task::getPosition)
                .orElse(null); // empty -> null

        if (max == null) {
            log.debug("[TASK][generateNextPosition] max가 null이므로 기본값 0으로 시작합니다.");
            return 0;
        }
        int next = max + 1;
        log.debug("[TASK][generateNextPosition] date={}, categoryId={} 의 max={} -> next={}",
                date, categoryId, max, next);

        return next;
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

        task.updateDueDate(request.getDate());

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
    public TaskOrderCategoryUpdateResponseDTO updateTaskOrders(Long userId, TaskOrderCategoryUpdateBulkRequestDTO request) {
        // 요청 유효성 검사
        List<TaskOrderCategoryUpdateRequestDTO> taskRequests = validateTaskOrdersRequest(request);

        // 유효성 검사
        Map<Long, Task> taskMap = loadAndValidateTasks(userId, taskRequests);

        // 카테고리 별 재정렬
        Set<Long> affectedCategories = applyCategoryWiseReordering(userId, taskRequests, taskMap);

        log.debug("[TASK] 순서 변경 완료 - affectedCategories={}, userId={}, taskCount={}",
                affectedCategories, userId, taskRequests.size());

        // DTO 생성
        return buildReorderResponse(taskRequests.size(), affectedCategories);
    }

    /**
     * 응답 DTO를 생성합니다.
     *
     * @param size
     * @param affectedCategories
     * @return
     */
    private TaskOrderCategoryUpdateResponseDTO buildReorderResponse(int size, Set<Long> affectedCategories) {
        return new TaskOrderCategoryUpdateResponseDTO(size, new ArrayList<>(affectedCategories));
    }

    /**
     * 할 일의 순서 변경 또는 카테고리를 변경합니다.
     *
     * @param userId
     * @param requestList
     * @param taskMap
     * @return
     */
    private Set<Long> applyCategoryWiseReordering(
            Long userId,
            List<TaskOrderCategoryUpdateRequestDTO> requestList,
            Map<Long, Task> taskMap
    ) {
        Set<Long> affectedCategories = new HashSet<>();

        // 1. 요청을 카테고리별로 groupBy
        Map<Long, List<TaskOrderCategoryUpdateRequestDTO>> groupedRequests =
                requestList.stream().collect(Collectors.groupingBy(TaskOrderCategoryUpdateRequestDTO::getCategoryId));

        for (Map.Entry<Long, List<TaskOrderCategoryUpdateRequestDTO>> entry : groupedRequests.entrySet()) {
            Long categoryId = entry.getKey();
            List<TaskOrderCategoryUpdateRequestDTO> requestTasks = entry.getValue();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.TASK_CATEGORY_NOT_FOUND));

            log.debug("[TASK][REORDER] categoryId={} - 요청된 task 수: {}", categoryId, requestTasks.size());

            // 2. 해당 카테고리의 전체 task 조회
            List<Task> allTasksInCategory = taskRepository.findAllByUserIdAndCategoryId(userId, categoryId);
            log.debug("[TASK][REORDER] categoryId={} - DB 내 전체 task 수: {}", categoryId, allTasksInCategory.size());

            // 3. 요청된 task 정렬용 Map
            Map<Long, Integer> requestedOrderMap = requestTasks.stream()
                    .collect(Collectors.toMap(TaskOrderCategoryUpdateRequestDTO::getTaskId, TaskOrderCategoryUpdateRequestDTO::getOrder));

            // 4. 요청된 task 정렬
            List<Task> requestedTasks = requestTasks.stream()
                    .map(dto -> taskMap.get(dto.getTaskId()))
                    .sorted(Comparator.comparingInt(task -> requestedOrderMap.get(task.getId())))
                    .toList();

            log.debug("[TASK][REORDER] 요청된 task 순서:");
            for (int i = 0; i < requestedTasks.size(); i++) {
                Task t = requestedTasks.get(i);
                log.debug("  - [{}] taskId={}, 요청 order={}", i, t.getId(), requestedOrderMap.get(t.getId()));
            }

            // 5. 요청되지 않은 task
            Set<Long> requestedIds = new HashSet<>(requestedOrderMap.keySet());
            List<Task> untouchedTasks = allTasksInCategory.stream()
                    .filter(task -> !requestedIds.contains(task.getId()))
                    .toList();

            log.debug("[TASK][REORDER] 포함되지 않은 task 수: {}", untouchedTasks.size());

            // 6. 최종 정렬 리스트 구성
            List<Task> finalSortedTasks = new ArrayList<>();
            finalSortedTasks.addAll(requestedTasks);
            finalSortedTasks.addAll(untouchedTasks);

            log.debug("[TASK][REORDER] 최종 정렬 후 position 재할당:");

            for (int i = 0; i < finalSortedTasks.size(); i++) {
                Task task = finalSortedTasks.get(i);

                // 카테고리 변경 필요 시
                if (!task.getCategory().getId().equals(categoryId)) {
                    log.debug("  - taskId={} 카테고리 변경: {} → {}", task.getId(), task.getCategory().getId(), categoryId);
                    task.updateCategory(category);
                }

                // position 재할당
                log.debug("  - taskId={}, 기존 position={}, 신규 position={}", task.getId(), task.getPosition(), i);
                task.updatePosition(i);
            }

            affectedCategories.add(categoryId);
        }

        log.debug("[TASK][REORDER] 전체 변경 완료 - userId={}, 변경된 카테고리 수: {}", userId, affectedCategories.size());
        return affectedCategories;
    }

    /**
     * Task가 유효한지 확인합니다.
     *
     * @param userId
     * @param taskRequests
     * @return
     */
    private Map<Long, Task> loadAndValidateTasks(
            Long userId,
            List<TaskOrderCategoryUpdateRequestDTO> taskRequests
    ) {
        // task id 추출
        List<Long> taskIds = taskRequests.stream().map(TaskOrderCategoryUpdateRequestDTO::getTaskId).toList();

        List<Task> tasks = taskRepository.findAllById(taskIds);

        // 요청 taskId 개수와 조회한 결과 개수가 다른 경우 예외 처리
        if (tasks.size() != taskIds.size()) {
            throw new GeneralException(ErrorStatus.TASK_NOT_FOUND);
        }

        for (Task task : tasks) {
            // task.userId와 요청한 userId가 다른 경우 예외 처리
            if (!task.getUser().getId().equals(userId)) {
                throw new GeneralException(ErrorStatus.TASK_FORBIDDEN);
            }
        }

        return tasks.stream().collect(Collectors.toMap(Task::getId, t -> t));
    }

    /**
     * Task 순서 변경 요청의 유효성을 검증합니다.
     */
    private List<TaskOrderCategoryUpdateRequestDTO> validateTaskOrdersRequest(TaskOrderCategoryUpdateBulkRequestDTO request) {
        List<TaskOrderCategoryUpdateRequestDTO> tasks = request.getTasks();
        if (tasks == null || tasks.isEmpty()) {
            log.warn("[TASK] 빈 taskOrder 요청");
            throw new GeneralException(ErrorStatus.TASK_INVALID_ORDER);
        }
        return tasks;
    }

    private Task findTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TASK_NOT_FOUND));
    }
}
