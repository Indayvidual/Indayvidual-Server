package com.indayvidual.server.domain.habit.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.habit.dto.request.CreateHabitRequestDTO;
import com.indayvidual.server.domain.habit.dto.request.UpdateHabitRequestDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitResponseDTO;
import com.indayvidual.server.domain.habit.entity.Habit;
import com.indayvidual.server.domain.habit.exception.HabitException;
import com.indayvidual.server.domain.habit.repository.HabitRepository;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.exception.UserException;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HabitCommandServiceImpl implements HabitCommandService {

	private final UserRepository userRepository;
	private final HabitRepository habitRepository;

	@Override
	public HabitResponseDTO createHabit(Long userId, CreateHabitRequestDTO request) {
		User currentUser = getCurrentUser(userId);

		Habit habit = Habit.createHabit(currentUser, request.getTitle(), request.getColorCode());

		habitRepository.save(habit);

		return HabitResponseDTO.from(habit);

	}

	@Override
	public HabitResponseDTO updateHabit(Long userId, Long habitId, UpdateHabitRequestDTO request) {
		User currentUser = getCurrentUser(userId);

		Habit habit = habitRepository.findById(habitId)
			.orElseThrow(() -> new HabitException(ErrorStatus.HABIT_NOT_FOUND));

		habit.updateHabit(currentUser, request.getTitle(), request.getColorCode());

		return HabitResponseDTO.from(habit);
	}

	@Override
	public Void deleteHabit(Long userId, Long habitId) {
		User currentUser = getCurrentUser(userId);

		Habit habit = habitRepository.findById(habitId)
			.orElseThrow(() -> new HabitException(ErrorStatus.HABIT_NOT_FOUND));

		if (!habit.getUser().equals(currentUser)) {
			throw new HabitException(ErrorStatus.HABIT_OWNER_MISMATCH);
		}

		habitRepository.delete(habit);

		return null;
	}

	private User getCurrentUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
	}
}
