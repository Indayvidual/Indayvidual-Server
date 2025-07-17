package com.indayvidual.server.domain.habitlog.entity;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.habit.entity.Habit;

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
@Table(name = "habit_log")
public class HabitLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "habit_log")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "habit_id")
	private Habit habit;

	@Builder.Default
	private Boolean isChecked = false;

	@Builder.Default
	private LocalDate checkedAt = LocalDate.now();

	//== 정적 팩토리 생성 메서드 ==//
	public static HabitLog createHabitLog(Habit habit) {
		HabitLog habitLog = HabitLog.builder()
			.habit(habit)
			.build();

		habit.getHabitLogs().add(habitLog);

		return habitLog;
	}

	//== 더티체킹 메서드 ==//
	public void updateCheck(Boolean isChecked) {
		this.isChecked = isChecked;
		this.checkedAt = isChecked ? LocalDate.now() : null;
	}
}
