package com.indayvidual.server.domain.habit.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.habit.dto.CreateHabitRequestDTO;
import com.indayvidual.server.domain.habit.entity.Habit;
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
	public Void createHabit(Long userId, CreateHabitRequestDTO request) {
		User currentUser = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

		Habit habit = Habit.createHabit(currentUser, request.getTitle(), request.getColor());

		habitRepository.save(habit);

		return null;

	}
}
