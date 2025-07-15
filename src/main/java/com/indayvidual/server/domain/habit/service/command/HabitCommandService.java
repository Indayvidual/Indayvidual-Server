package com.indayvidual.server.domain.habit.service.command;

import com.indayvidual.server.domain.habit.dto.CreateHabitRequestDTO;

public interface HabitCommandService {

	Void createHabit(Long userId, CreateHabitRequestDTO request);
}
