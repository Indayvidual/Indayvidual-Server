package com.indayvidual.server.domain.habit.service.query;

import com.indayvidual.server.domain.habit.dto.response.HabitSliceResponseDTO;

public interface HabitQueryService {

	HabitSliceResponseDTO getHabits(Long userId, Integer page, Integer size);
}
