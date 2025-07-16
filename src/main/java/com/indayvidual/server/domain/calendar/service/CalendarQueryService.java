package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface CalendarQueryService {
    List<GetMonthlyCalendarResponseDto> getMonthlyCalendar(int year, int month, Long userId);
}
