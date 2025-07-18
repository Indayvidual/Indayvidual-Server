package com.indayvidual.server.domain.habit.dto.response;

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
public class HabitWeeklyChecksResponseDTO {

	private Long habitId;

	private String title;

	private String colorCode;

	private List<Boolean> checks;

	public static HabitWeeklyChecksResponseDTO from(Habit habit) {
		return HabitWeeklyChecksResponseDTO.builder()
			.habitId(habit.getId())
			.title(habit.getTitle())
			.colorCode(habit.getColorCode())
			.checks(habit.getHabitLogs().stream()
				.map(HabitLog::getIsChecked)
				.toList()
			)
			.build();
	}

}
