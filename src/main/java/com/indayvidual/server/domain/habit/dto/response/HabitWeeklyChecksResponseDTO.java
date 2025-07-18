package com.indayvidual.server.domain.habit.dto.response;

import java.util.List;

import com.indayvidual.server.domain.habit.entity.Habit;

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

	private List<HabitDateChecksResponseDTO> checkedAtList;

	public static HabitWeeklyChecksResponseDTO from(Habit habit) {
		return HabitWeeklyChecksResponseDTO.builder()
			.habitId(habit.getId())
			.title(habit.getTitle())
			.colorCode(habit.getColorCode())
			.checkedAtList(
				habit.getHabitLogs().stream()
					.map(HabitDateChecksResponseDTO::from).toList()
			)
			.build();
	}

}
