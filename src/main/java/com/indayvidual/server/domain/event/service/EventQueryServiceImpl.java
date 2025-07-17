package com.indayvidual.server.domain.event.service;

import com.indayvidual.server.domain.event.converter.EventConverter;
import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
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
    public List<GetDayEventResponseDto> getDayEvents(LocalDate date, Long userId) {
        List<Event> events = eventRepository.findByUserIdAndEventDateOrderByStartTimeAsc(userId, date);

        return events.stream()
                .map(eventConverter::toGetDayEventResponse)
                .collect(Collectors.toList());
    }
}
