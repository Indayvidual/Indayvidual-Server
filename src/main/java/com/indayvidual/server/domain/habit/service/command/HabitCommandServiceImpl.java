package com.indayvidual.server.domain.habit.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.habit.dto.request.CreateHabitRequestDTO;
import com.indayvidual.server.domain.habit.dto.request.ToggleCheckRequestDTO;
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

		/**
		 * 습관과 그 습관의 로그도 같이 생성
		 */
		Habit habit = Habit.createHabit(currentUser, request.getTitle(), request.getColorCode());

		habitRepository.save(habit);

		return HabitResponseDTO.from(habit);

	}

	@Override
	public HabitResponseDTO updateHabit(Long userId, Long habitId, UpdateHabitRequestDTO request) {
		User currentUser = getCurrentUser(userId);

		Habit habit = getHabit(habitId);

		habit.updateHabit(currentUser, request.getTitle(), request.getColorCode());

		return HabitResponseDTO.from(habit);
	}

	@Override
	public Void deleteHabit(Long userId, Long habitId) {
		User currentUser = getCurrentUser(userId);

		Habit habit = getHabit(habitId);

		if (habit.canDeleteBy(currentUser)) {
			habitRepository.delete(habit);
		}

		return null;
	}

	@Override
	public HabitResponseDTO updateHabitCheck(Long userId, Long habitId, ToggleCheckRequestDTO request) {
		User currentUser = getCurrentUser(userId);
		Habit habit = getHabit(habitId);

		habit.updateHabitCheck(currentUser, request.getDate(), request.getChecked());

		return HabitResponseDTO.of(habit, request.getChecked(), request.getDate());
	}

	private Habit getHabit(Long habitId) {
		return habitRepository.findById(habitId)
			.orElseThrow(() -> new HabitException(ErrorStatus.HABIT_NOT_FOUND));
	}

	private User getCurrentUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
	}
}
