package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
import com.indayvidual.server.domain.event.converter.EventConverter;
import com.indayvidual.server.domain.event.entity.Event;
import com.indayvidual.server.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarQueryServiceImpl implements CalendarQueryService {

    private final EventRepository eventRepository;

    @Override
    public List<GetMonthlyCalendarResponseDto> getMonthlyCalendar(int year, int month, Long userId) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<LocalDate> eventDates = eventRepository.findEventDatesByUserIdAndDateRange(userId, startDate, endDate);
        Set<LocalDate> eventDateSet = eventDates.stream().collect(Collectors.toSet());

        // TODO: 할일 기능 구현 후 할일 날짜 조회
        // Set<LocalDate> todoDataSet = todoRepository.findTodoDatesByUserIdAndDateRange(userId, startDate, endDate);

        List<GetMonthlyCalendarResponseDto> result = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            result.add(GetMonthlyCalendarResponseDto.builder()
                    .date(currentDate)
                    .hasEvent(eventDateSet.contains(currentDate))
                    .hasTodo(false) // TODO: 할일 기능 구현 후 수정 - todoDateSet.contains(currentDate)
                    .build());
            currentDate = currentDate.plusDays(1);
        }
        return result;
    }
}
