package com.indayvidual.server.domain.habit.service.query;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.habit.dto.response.HabitMonthlyChecksResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitSliceResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitWeeklyChecksResponseDTO;
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
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Slice<Habit> habitSlice = habitRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId, pageable);

		return HabitSliceResponseDTO.from(habitSlice.map(HabitResponseDTO::from));

	}

	@Override
	public List<HabitResponseDTO> getDailyHabitCheckedState(Long userId, LocalDate date) {

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

	@Override
	public List<HabitWeeklyChecksResponseDTO> getWeeklyChecks(Long userId, LocalDate startDate) {

		List<Habit> allHabitsWithLogsOnDateRange = habitRepository.findAllHabitsWithLogsOnDateRange(userId,
			startDate, startDate.plusDays(6));

		return allHabitsWithLogsOnDateRange.stream()
			.map(HabitWeeklyChecksResponseDTO::from)
			.toList();

	}

	@Override
	public List<HabitMonthlyChecksResponseDTO> getMonthlyChecks(Long userId, YearMonth yearMonth) {

		List<Habit> habits = habitRepository.findAllHabitsWithLogsOnMonth(userId, yearMonth);

		return habits.stream()
			.map(HabitMonthlyChecksResponseDTO::from)
			.toList();
	}

	private User getCurrentUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
	}
}
