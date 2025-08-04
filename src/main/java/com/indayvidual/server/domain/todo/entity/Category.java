package com.indayvidual.server.domain.todo.entity;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.GeneralException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(name = "color_code", nullable = false, length = 10)
    private String color;

    // --- 도메인 로직 ---
    public void updateTitle(String title) {
        if (title == null || title.isBlank()) throw new GeneralException(ErrorStatus.TASK_CATEGORY_TITLE_EMPTY);
        log.debug("[CATEGORY] 제목 변경 - before: {}, after: {}", this.title, title);
        this.title = title;
    }

    public void updateColor(String color) {
        log.debug("[CATEGORY] 색상 변경 - before: {}, after: {}", this.color, color);
        if (color == null || !color.startsWith("#")) throw new GeneralException(ErrorStatus.COLOR_INVALID);
        this.color = color;
    }

    @Builder
    private Category(User user, String title, String color) {
        this.user = user;
        this.title = title;
        this.color = color;
    }
}
