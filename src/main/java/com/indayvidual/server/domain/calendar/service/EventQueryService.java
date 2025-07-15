package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
import com.indayvidual.server.domain.calendar.entity.Event;

import java.util.List;

public interface EventQueryService {
    Event findByIdAndUserId(Long eventId, Long userId);
    boolean existsByIdAndUserId(Long eventId, Long userId);

    List<GetMonthlyCalendarResponseDto> getMonthlyCalendar(int year, int month, Long userId);
}
