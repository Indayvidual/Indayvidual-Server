package com.indayvidual.server.domain.habit.dto.response;

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
public class HabitResponseDTO {

	private String title;

	private boolean isChecked;

	private String colorCode;

	public static HabitResponseDTO from(Habit habit) {
		return HabitResponseDTO.builder()
			.title(habit.getTitle())
			.isChecked(habit.getIsChecked())
			.colorCode(habit.getColorCode())
			.build();
	}

}
