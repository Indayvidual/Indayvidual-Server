package com.indayvidual.server.domain.habit.service.query;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.habit.dto.response.HabitResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitSliceResponseDTO;
import com.indayvidual.server.domain.habit.entity.Habit;
import com.indayvidual.server.domain.habit.repository.HabitRepository;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.exception.UserException;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HabitQueryServiceImpl implements HabitQueryService {

	private final UserRepository userRepository;
	private final HabitRepository habitRepository;

	@Override
	public HabitSliceResponseDTO getHabits(Long userId, Integer page, Integer size) {
		int pageNumber = (page != null && page >= 0) ? page : 0;
		int pageSize = Utils.validatePageSize(size);

		// 사용자 검증
		getCurrentUser(userId);

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Slice<Habit> habitSlice = habitRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId, pageable);

		return HabitSliceResponseDTO.from(habitSlice.map(HabitResponseDTO::from));

	}

	@Override
	public List<HabitResponseDTO> getDailyHabitCheckedState(Long userId, LocalDate date) {
		User currentUser = getCurrentUser(userId);

		List<Habit> habits = habitRepository.findAllHabitsWithLogsOnDate(userId, date);

		return habits.stream()
			.map(habit -> {
				boolean isChecked = habit.getHabitLogs().stream()
					.anyMatch(log -> log.getCheckedAt() != null
						&& log.getCheckedAt().equals(date)
						&& log.getIsChecked());

				return HabitResponseDTO.of(habit, isChecked, date);
			})
			.collect(Collectors.toList());
	}

	private User getCurrentUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
	}
}
