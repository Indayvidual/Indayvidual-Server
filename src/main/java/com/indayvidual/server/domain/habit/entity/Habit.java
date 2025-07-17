package com.indayvidual.server.domain.habit.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.habit.exception.HabitException;
import com.indayvidual.server.domain.habitlog.entity.HabitLog;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.global.api.code.status.ErrorStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<HabitLog> habitLogs = new ArrayList<>();

	private String title; // 습관 이름

	private String colorCode; // 색상 코드

	//== 정정 팩토리 메서드 ==//
	public static Habit createHabit(User user, String title, String colorCode) {
		Habit habit = Habit.builder()
			.user(user)
			.title(title)
			.colorCode(colorCode)
			.build();

		user.getHabits().add(habit);

		HabitLog.createHabitLog(habit);

		return habit;

	}

	//== 더티체킹 메서드 ==//
	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateColorCode(String colorCode) {
		this.colorCode = colorCode;
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

	public void updateHabitCheck(User user, LocalDate checkDate, Boolean checked) {
		ensureOwnership(user);

		// 먼저 찾기
		HabitLog targetLog = null;
		for (HabitLog log : habitLogs) {
			if (log.getCheckedAt().equals(checkDate)) {  // logDate로 변경
				targetLog = log;
				break;
			}
		}

		// 없으면 생성
		if (targetLog == null) {
			targetLog = HabitLog.builder()
				.habit(this)
				.checkedAt(checkDate)  // 체크할 날짜 설정
				.isChecked(false)
				.build();
			this.habitLogs.add(targetLog);
		}

		// 상태 업데이트
		targetLog.updateCheck(checked);
	}

}
