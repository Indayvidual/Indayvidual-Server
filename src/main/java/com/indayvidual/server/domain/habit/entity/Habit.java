package com.indayvidual.server.domain.habit.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.habit.exception.HabitException;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.global.api.code.status.ErrorStatus;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "habit")
public class Habit extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "habit_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String title; // 습관 이름

	private String colorCode; // 색상 코드

	@Builder.Default
	private Boolean isChecked = false;

	@Builder.Default
	private LocalDateTime checkedAt = LocalDateTime.now();

	//== 정정 팩토리 메서드 ==//
	public static Habit createHabit(User user, String title, String colorCode) {
		Habit habit = Habit.builder()
			.user(user)
			.title(title)
			.colorCode(colorCode)
			.build();

		user.getHabits().add(habit);

		return habit;

	}

	//== 수정 메서드 ==//
	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public void updateChecked(Boolean isChecked) {
		this.isChecked = isChecked;
		this.checkedAt = LocalDateTime.now();
	}

	//== 소유자 확인 메서드 ==//
	public boolean isOwnerBy(User user) {
		return this.user.equals(user);
	}

	public void ensureOwnership(User user) {
		if (!isOwnerBy(user)) {
			throw new HabitException(ErrorStatus.HABIT_OWNER_MISMATCH);
		}
	}

	//== 비즈니스 메서드 ==//
	public void updateHabit(User user, String title, String colorCode) {
		ensureOwnership(user);

		// 이미 title과 colorCode는 검증이 끝남 -> dto
		updateTitle(title);
		updateColorCode(colorCode);

	}

	public boolean canDeleteBy(User user) {
		ensureOwnership(user);

		user.getHabits().remove(this);

		return true;
	}
}
