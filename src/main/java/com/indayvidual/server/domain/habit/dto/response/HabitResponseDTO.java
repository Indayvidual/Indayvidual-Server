package com.indayvidual.server.domain.habit.dto.response;

import java.time.LocalDateTime;

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

	private String colorCode;

	private boolean isChecked;

	private LocalDateTime checkedAt;

	public static HabitResponseDTO from(Habit habit) {
		return HabitResponseDTO.builder()
			.title(habit.getTitle())
			.colorCode(habit.getColorCode())
			.isChecked(habit.getIsChecked())
			.checkedAt(habit.getCheckedAt())
			.build();
	}

}
