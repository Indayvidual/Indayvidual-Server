package com.indayvidual.server.domain.habit.service.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.habit.dto.response.HabitResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitSliceResponseDTO;
import com.indayvidual.server.domain.habit.entity.Habit;
import com.indayvidual.server.domain.habit.repository.HabitRepository;
import com.indayvidual.server.global.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HabitQueryServiceImpl implements HabitQueryService {

	private final HabitRepository habitRepository;

	@Override
	public HabitSliceResponseDTO getHabits(Long userId, Integer page, Integer size) {
		int pageNumber = (page != null && page >= 0) ? page : 0;
		int pageSize = Utils.validatePageSize(size);

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Slice<Habit> habitSlice = habitRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId, pageable);

		return HabitSliceResponseDTO.from(habitSlice.map(HabitResponseDTO::from));

	}
}
