package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
import com.indayvidual.server.domain.calendar.exception.CalendarException;
import com.indayvidual.server.domain.event.entity.Event;
import com.indayvidual.server.domain.event.repository.EventRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarQueryServiceImpl implements CalendarQueryService {

    private final EventRepository eventRepository;

    @Override
    public List<GetMonthlyCalendarResponseDto> getMonthlyCalendar(int year, int month, Long userId) {
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.of(year, month);
        } catch (DateTimeException e) {
            throw new CalendarException(ErrorStatus.CALENDAR_INVALID_DATE_INPUT);
        }

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Event> events = eventRepository.findByUserIdAndEventDateBetween(userId, startDate, endDate);

        Map<LocalDate, List<String>> dateColorsMap = events.stream()
                .collect(Collectors.groupingBy(
                        Event::getEventDate,
                        Collectors.mapping(Event::getColorCode, Collectors.toList())
                ));

        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(yearMonth.lengthOfMonth())
                .map(date -> {
                    List<String> colors = dateColorsMap.getOrDefault(date, Collections.emptyList());
                    return GetMonthlyCalendarResponseDto.builder()
                            .date(date)
                            .colors(colors)
                            .build();
                })
                .collect(Collectors.toList());
    }
}

