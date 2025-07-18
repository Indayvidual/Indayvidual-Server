package com.indayvidual.server.domain.habit.dto.response;

import java.time.LocalDate;

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
public class HabitDateChecksResponseDTO {

	private LocalDate checkedAt;

	private Boolean isChecked;

	public static HabitDateChecksResponseDTO from(HabitLog habitLog) {
		return HabitDateChecksResponseDTO.builder()
			.checkedAt(habitLog.getCheckedAt())
			.isChecked(habitLog.getIsChecked())
			.build();
	}
	
}
