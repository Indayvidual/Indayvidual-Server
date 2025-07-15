package com.indayvidual.server.domain.habit.service.command;

import com.indayvidual.server.domain.habit.dto.request.CreateHabitRequestDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitResponseDTO;

public interface HabitCommandService {

	HabitResponseDTO createHabit(Long userId, CreateHabitRequestDTO request);
}
