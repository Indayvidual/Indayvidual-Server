package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.converter.EventConverter;
import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
import com.indayvidual.server.domain.calendar.entity.Event;
import com.indayvidual.server.domain.calendar.repository.EventRepository;
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
public class EventQueryServiceImpl implements EventQueryService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    @Override
    public Event findByIdAndUserId(Long eventId, Long userId) {
        return eventRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("해당 일정을 찾을 수 없습니다."));
    }

    @Override
    public boolean existsByIdAndUserId(Long eventId, Long userId) {
        return eventRepository.existsByIdAndUserId(eventId, userId);
    }

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

    @Override
    public List<GetDayEventResponseDto> getDayEvents(LocalDate date, Long userId) {
        List<Event> events = eventRepository.findByUserIdAndEventDateOrderByStartTimeAsc(userId, date);

        return events.stream()
                .map(eventConverter::toGetDayEventResponse)
                .collect(Collectors.toList());
    }
}
