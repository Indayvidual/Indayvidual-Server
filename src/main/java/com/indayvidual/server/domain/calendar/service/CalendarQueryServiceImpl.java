package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
import com.indayvidual.server.domain.event.entity.Event;
import com.indayvidual.server.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
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

        List<Event> events = eventRepository.findByUserIdAndEventDateBetween(userId, startDate, endDate);

        Map<LocalDate, List<String>> dateColorsMap = events.stream()
                .collect(Collectors.groupingBy(
                        Event::getEventDate,
                        LinkedHashMap::new,
                        Collectors.mapping(Event::getColorCode, Collectors.toList())
                ));

        List<GetMonthlyCalendarResponseDto> result = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            List<String> colors = dateColorsMap.getOrDefault(currentDate, Collections.emptyList());

            result.add(GetMonthlyCalendarResponseDto.builder()
                    .date(currentDate)
                    .colors(colors)
                    .build());
            currentDate = currentDate.plusDays(1);
        }

        return result;
    }
}
