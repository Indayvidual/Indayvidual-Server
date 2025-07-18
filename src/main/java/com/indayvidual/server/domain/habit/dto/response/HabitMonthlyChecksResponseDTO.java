package com.indayvidual.server.domain.habit.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.indayvidual.server.domain.habit.entity.Habit;
import com.indayvidual.server.domain.habitlog.entity.HabitLog;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HabitMonthlyChecksResponseDTO {

	private Long habitId;

	private String title;

	private String colorCode;

	private List<LocalDate> checkedDates;

	public static HabitMonthlyChecksResponseDTO from(Habit habit) {
		return HabitMonthlyChecksResponseDTO.builder()
			.habitId(habit.getId())
			.title(habit.getTitle())
			.colorCode(habit.getColorCode())
			.checkedDates(habit.getHabitLogs().stream()
				.map(HabitLog::getCheckedAt)
				.toList())
			.build();
	}
}
