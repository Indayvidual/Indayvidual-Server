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

    // 유니크키(user, category, dueDate, position) 충돌 방지용 임시 position 베이스
    private static final int TEMP_BASE = 1_000_000;

    // 버킷 키: (categoryId, dueDate) – userId는 메서드 인자로 고정
    private record BucketKey(Long categoryId, LocalDate dueDate) {
    }

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
     * 카테고리 내 할 일 순서를 변경, 카테고리를 이동합니다.
     */
    @Transactional
    public TaskOrderCategoryUpdateResponseDTO updateTaskOrderAndCategory(Long userId, TaskOrderCategoryUpdateBulkRequestDTO request) {
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
     * <p>
     * - 버킷 단위: (userId, categoryId, dueDate)
     * - 슬롯 방식: order를 최종 인덱스(절대 위치)로 해석
     * - 부분 요청 허용: 요청에 없는 것은 기존 순서대로 빈 칸 채움
     * - 유니크키 충돌 방지: 임시 pos → flush → 최종 pos → flush
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

        // 0) 모든 요청의 타겟 버킷 맵 (taskId -> (categoryId, dueDate))
        Map<Long, BucketKey> targetByTaskId = requestList.stream().collect(Collectors.toMap(
                TaskOrderCategoryUpdateRequestDTO::getTaskId,
                dto -> {
                    Task t = taskMap.get(dto.getTaskId());
                    LocalDate due = t.getDueDate();
                    return new BucketKey(dto.getCategoryId(), due);
                }
        ));

        // 1) 요청을 (categoryId, dueDate) 버킷으로 그룹핑
        Map<BucketKey, List<TaskOrderCategoryUpdateRequestDTO>> groupedRequests =
                requestList.stream().collect(Collectors.groupingBy(dto -> {
                    Task t = taskMap.get(dto.getTaskId());
                    LocalDate due = t.getDueDate();
                    return new BucketKey(dto.getCategoryId(), due);
                }));

        for (Map.Entry<BucketKey, List<TaskOrderCategoryUpdateRequestDTO>> entry : groupedRequests.entrySet()) {
            BucketKey bucket = entry.getKey();
            Long categoryId = bucket.categoryId();
            LocalDate dueDate = bucket.dueDate();
            List<TaskOrderCategoryUpdateRequestDTO> requestTasks = entry.getValue();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.TASK_CATEGORY_NOT_FOUND));

            log.debug("[TASK][REORDER] bucket(categoryId={}, dueDate={}) - 요청 task 수: {}", categoryId, dueDate, requestTasks.size());

            // 2-1) 현재 버킷의 전체 tasks (현재 카테고리 기준) 조회 + position 오름차순 정렬
            List<Task> allTasksInBucket = taskRepository
                    .findAllByUserIdAndCategoryIdAndDueDate(userId, categoryId, dueDate)
                    .stream()
                    .sorted(Comparator.comparingInt(Task::getPosition))
                    .toList();

            log.debug("[TASK][REORDER] bucket(categoryId={}, dueDate={}) - DB 내 전체 task 수: {}", categoryId, dueDate, allTasksInBucket.size());
            log.debug("[TASK][REORDER] 기존 position 오름차순 정렬 완료 - 첫 position={}, 마지막 position={}",
                    allTasksInBucket.isEmpty() ? null : allTasksInBucket.get(0).getPosition(),
                    allTasksInBucket.isEmpty() ? null : allTasksInBucket.get(allTasksInBucket.size() - 1).getPosition());

            // 2-2) base = (staying) U (incoming)
            //      * staying = 현재 버킷에 있고, 다른 버킷으로 이동하지 않는 항목
            //      * incoming = 다른 버킷에 있었고, 요청에서 현재로 이동하도록 지정된 항목
            Set<Long> requestedIds = requestTasks.stream().map(TaskOrderCategoryUpdateRequestDTO::getTaskId).collect(Collectors.toSet());
            List<Task> incoming = requestTasks.stream().map(r -> taskMap.get(r.getTaskId())).toList();
            List<Task> staying = allTasksInBucket.stream()
                    .filter(t -> {
                        BucketKey target = targetByTaskId.get(t.getId());
                        // target이 없으면(이번 요청에 없음) 남아있는 것으로 간주
                        return (target == null) || target.equals(bucket);
                    })
                    .toList();

            // base = staying ∪ incoming (taskId 기준 distinct)
            Map<Long, Task> baseMap = new LinkedHashMap<>();
            for (Task t : staying) baseMap.put(t.getId(), t);
            for (Task t : incoming) baseMap.put(t.getId(), t);
            List<Task> base = new ArrayList<>(baseMap.values());
            int n = base.size();

            log.debug("[TASK][REORDER] base 크기 확정 - staying={}, incoming={}, base={}", staying.size(), incoming.size(), n);

            // 2-3) 슬롯 구성: 요청 order 우선 배치, 중복/범위외는 overflow
            Task[] slots = new Task[n];
            List<Task> overflow = new ArrayList<>();
            for (TaskOrderCategoryUpdateRequestDTO r : requestTasks) {
                Task t = taskMap.get(r.getTaskId());
                Integer target = r.getOrder();
                if (target == null || target < 0 || target >= n) {
                    overflow.add(t); // 범위 밖은 뒤에 채움
                    continue;
                }
                if (slots[target] == null) {
                    slots[target] = t; // 지정 슬롯에 배치
                } else {
                    overflow.add(t);   // 중복 order는 뒤에 채움
                }
            }

            // 2-4) 빈 칸을 untouched(= base 중 요청에 포함되지 않은 것) → overflow 순으로 채움
            Iterator<Task> untouchedIt = base.stream().filter(t -> !requestedIds.contains(t.getId())).iterator();
            Iterator<Task> overflowIt = overflow.iterator();
            for (int i = 0; i < n; i++) {
                if (slots[i] == null) {
                    if (untouchedIt.hasNext()) {
                        slots[i] = untouchedIt.next();
                    } else if (overflowIt.hasNext()) {
                        slots[i] = overflowIt.next();
                    }
                }
            }

            // 2-5) 최종 리스트 확정
            List<Task> finalSortedTasks = Arrays.stream(slots).filter(Objects::nonNull).toList();
            log.debug("[TASK][REORDER] 최종 정렬 후 position 재할당 (bucket={},{}):",
                    categoryId, dueDate);

            // 2-6) 카테고리 변경 및 임시 position 부여 → flush (유니크키 충돌 방지)
            for (int i = 0; i < finalSortedTasks.size(); i++) {
                Task task = finalSortedTasks.get(i);

                // 카테고리 변경 필요 시
                if (!task.getCategory().getId().equals(categoryId)) {
                    log.debug("  - taskId={} 카테고리 변경: {} → {}", task.getId(), task.getCategory().getId(), categoryId);
                    task.updateCategory(category);
                }
                task.updatePosition(TEMP_BASE + i);
            }
            taskRepository.flush(); // 중간 flush로 일시 중복 상태 제거

            // 2-7) 최종 pos 0..N-1 부여 → flush
            for (int i = 0; i < finalSortedTasks.size(); i++) {
                Task task = finalSortedTasks.get(i);
                log.debug("  - taskId={}, 최종 position={}", task.getId(), i);
                task.updatePosition(i);
            }
            taskRepository.flush();

            affectedCategories.add(categoryId);
        }

        log.debug("[TASK][REORDER] 전체 변경 완료 - userId={}, 변경된 카테고리 수: {}",
                userId, affectedCategories.size());

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
        // taskId 중복 및 order 음수 방지
        Set<Long> seen = new HashSet<>();
        for (TaskOrderCategoryUpdateRequestDTO t : tasks) {
            if (t.getTaskId() == null || !seen.add(t.getTaskId())) {
                log.warn("[TASK] taskId 중복 또는 null");
                throw new GeneralException(ErrorStatus.TASK_INVALID_ORDER);
            }
            if (t.getOrder() == null || t.getOrder() < 0) {
                log.warn("[TASK] order가 null 또는 음수");
                throw new GeneralException(ErrorStatus.TASK_INVALID_ORDER);
            }
            if (t.getCategoryId() == null) {
                log.warn("[TASK] categoryId가 null");
                throw new GeneralException(ErrorStatus.TASK_INVALID_ORDER);
            }
        }
        return tasks;
    }

    private Task findTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TASK_NOT_FOUND));
    }
}
