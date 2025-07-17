package com.indayvidual.server.domain.todo.entity;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 연관 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;

    // 날짜
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    // 카테고리 내 순서. 0부터 시작
    @Column(name = "position")
    private Integer position;

    // --- 도메인 로직 ---
    public void toggleChecked() {
        this.isChecked = !this.isChecked;
    }

    public void updateTitle(String title) {
        log.debug("[TASK] 제목 변경 - before: {}, after: {}", this.title, title);
        this.title = title;
    }

    public void updateDueDate(LocalDate dueDate) {
        log.debug("[TASK] 날짜 변경 - before: {}, after: {}", this.dueDate, dueDate);
        this.dueDate = dueDate;
    }

    public void updatePosition(int position) {
        log.debug("[TASK] 순서 변경 - before: {}, after: {}", this.position, position);
        this.position = position;
    }

    public static Task create(User user, Category category, String title, LocalDate dueDate, int position) {
        return Task.builder()
                .user(user)
                .category(category)
                .title(title)
                .dueDate(dueDate)
                .position(position)
                .isChecked(false) // 생성 시 기본값 고정
                .build();
    }

    @Builder
    private Task(User user, String title, boolean isChecked, LocalDate dueDate, Category category, Integer position) {
        this.user = user;
        this.title = title;
        this.isChecked = isChecked;
        this.dueDate = dueDate;
        this.position = position;
        this.category = category;
    }
}