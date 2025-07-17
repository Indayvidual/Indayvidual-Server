package com.indayvidual.server.domain.event.service;

import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
import com.indayvidual.server.domain.event.entity.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventQueryService {
    Event findByIdAndUserId(Long eventId, Long userId);
    boolean existsByIdAndUserId(Long eventId, Long userId);
    List<GetDayEventResponseDto> getDayEvents(LocalDate date, Long userId);
}
