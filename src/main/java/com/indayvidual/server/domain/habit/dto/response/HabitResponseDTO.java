package com.indayvidual.server.domain.habit.dto.response;

import java.time.LocalDate;

import com.indayvidual.server.domain.habit.entity.Habit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "습관 내용 관리 DTO")
public class HabitResponseDTO {

	@Schema(description = "습관 ID", example = "1")
	private Long habitId;

	@Schema(description = "습관 이름", example = "운동")
	private String title;

	@Schema(description = "색상 코드", example = "#FF5733")
	private String colorCode;

	@Schema(description = "체크 여부", example = "true")
	private boolean isChecked;

	@Schema(description = "언제 체크했는지", example = "2025-01-01")
	private LocalDate checkedAt;

	public static HabitResponseDTO from(Habit habit) {
		return HabitResponseDTO.builder()
			.title(habit.getTitle())
			.colorCode(habit.getColorCode())
			.isChecked(false)
			.checkedAt(null)
			.build();
	}

	public static HabitResponseDTO of(Habit habit, boolean isChecked, LocalDate checkedAt) {
		return HabitResponseDTO.builder()
			.title(habit.getTitle())
			.colorCode(habit.getColorCode())
			.isChecked(isChecked)
			.checkedAt(checkedAt)
			.build();
	}

}
