package com.indayvidual.server.domain.habit.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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

}
