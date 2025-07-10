package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.calendar.entity.Event;

public interface EventCommandService {
    Event createEvent(CreateEventRequestDto request, Long userId);
}
