package com.indayvidual.server.domain.habit.service.query;

import java.time.LocalDate;
import java.util.List;

import com.indayvidual.server.domain.habit.dto.response.HabitResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitSliceResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitWeeklyChecksResponseDTO;

public interface HabitQueryService {

	HabitSliceResponseDTO getHabits(Long userId, Integer page, Integer size);

	List<HabitResponseDTO> getDailyHabitCheckedState(Long userId, LocalDate date);

	List<HabitWeeklyChecksResponseDTO> getWeeklyChecks(Long userId, LocalDate startDate);
}
